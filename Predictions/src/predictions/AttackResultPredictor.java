package predictions;

import java.util.LinkedList;
import java.util.List;

import de.core.GameInformation;

/**
 * Calculates the probability of a shot on goal / ball out of bounds / ball loss
 * after the ongoing attack.
 * 
 */
public class AttackResultPredictor extends Predictor {

	public static final String TAG = "[Predictions][AttackResultPredictor] ";

	private static final int PLAYER_RADIUS = 20;

	private int idOfLastPlayerWithBall = -1;

	private int passCounter = 0;

	private boolean arffCreated = false;

	private List<Integer> velocityHistory = new LinkedList<Integer>();
	private long lastGameTime = -1;
	private int lastBallYPosition = -1; // TODO x or y?

	private long lastBallLossTimestamp = -1;
	private long lastBallOutsideTimestamp = -1;
	private long lastShotOnGoalTimestamp = -1;

	private int ballLossCounter = 0;
	private int noBallLossCounter = 0;

	/**
	 * Instantiates the attack result predictor.
	 * 
	 * @param learner
	 *            the classifier to use for prediction
	 */
	public AttackResultPredictor(Learner learner) {
		super(learner);
	}

	@Override
	public void init() {
		predictionInstance = new AttackResultPredictionInstance();
		learner.init(predictionInstance.getHeader());
	}

	@Override
	public void update(GameInformation gameInformation) {
		System.out.println(TAG + " - - - attack result prediction update with "
				+ learner.getClass().getName() + " - - - ");

		// TODO create ARFF file at the very end
		if (Utils.ARFF_WRITING_MODE && !arffCreated)
			if (learner.getAccumulatedInstances().size() == 200) { // 307 instances
				arffCreated = true;
				Utils.createArffFileFromInstances(learner
						.getAccumulatedInstances(), this.getClass().getName()
						+ "_" + predictionInstance.getClass().getName());
			}

		if (gameInformation.getCurrentBallPossessionPlayer() == null) {
			if (Utils.DEBUGGING)
				System.out.println(TAG
						+ "Prediction update failed: no player has ball");
			return;
		}

		int idOfCurrentPlayerWithBall = gameInformation
				.getCurrentBallPossessionPlayer().getId();

		// pass occurred
		if (idOfCurrentPlayerWithBall != idOfLastPlayerWithBall
				&& idOfLastPlayerWithBall != -1) {
			passCounter++;
		}

		// init attack values
		long currentBallLossTimestamp = gameInformation
				.getLastBallLossTimeStamp();
		long currentBallOutsideTimestamp = gameInformation
				.getLastBallOutsideTimeStamp();
		long currentShotOnGoalTimestamp = gameInformation
				.getLastShotOnGoalTimeStamp();

		if (lastBallLossTimestamp == -1) {
			lastBallLossTimestamp = currentBallLossTimestamp;
			lastBallOutsideTimestamp = currentBallOutsideTimestamp;
			lastShotOnGoalTimestamp = currentShotOnGoalTimestamp;
		}

		// init velocity values
		long currentGameTime = gameInformation.getCurrentGameTime();
		int currentBallXPosition = gameInformation
				.getCurrentBallPossessionPlayer().getPositionX();

		if (lastGameTime == -1) {
			lastBallYPosition = gameInformation
					.getCurrentBallPossessionPlayer().getPositionX();
			lastGameTime = gameInformation.getCurrentGameTime();
		}

		// calculate velocity
		int velocity = (int) (Math
				.abs(currentBallXPosition - lastBallYPosition) / (currentGameTime
				- lastGameTime + 1));
		velocityHistory.add(velocity);
		if (velocityHistory.size() > 5)
			velocityHistory.remove(0);
		int averageVelocity = 0;
		for (int velocityValue : velocityHistory)
			averageVelocity += velocityValue / velocityHistory.size();

		lastBallYPosition = currentBallXPosition;
		lastGameTime = currentGameTime;

		// update instance
		((AttackResultPredictionInstance) predictionInstance)
				.setAttributes(gameInformation
						.getTeammatesInArea(PLAYER_RADIUS), gameInformation
						.getOpponentsInArea(PLAYER_RADIUS), gameInformation
						.getPlayerPassesSuccessful(idOfLastPlayerWithBall),
						gameInformation
								.getPlayerPassesMissed(idOfLastPlayerWithBall),
						gameInformation
								.getPlayerBallContacts(idOfLastPlayerWithBall),
						"" + idOfLastPlayerWithBall, ""
								+ gameInformation
										.getCurrentBallPossessionPlayer()
										.getId(), (int) gameInformation
								.getDistanceOfNearestTeammate(),
						gameInformation.getCurrentBallPossessionPlayer()
								.getPositionX(), gameInformation
								.getCurrentBallPossessionPlayer()
								.getPositionY(), Math.round(gameInformation
								.getPlayerDistance(idOfLastPlayerWithBall)),
						gameInformation.isPlayerOnOwnSide(gameInformation
								.getCurrentBallPossessionPlayer()),
						passCounter, averageVelocity);

		// check if class event occurred
		String eventClass = "";
		if (lastBallLossTimestamp != currentBallLossTimestamp)
			eventClass = AttackResultPredictionInstance.CLASS_BALL_LOSS;
		else if (lastBallOutsideTimestamp != currentBallOutsideTimestamp)
			eventClass = AttackResultPredictionInstance.CLASS_BALL_OUT_OF_BOUNDS;
		else if (lastShotOnGoalTimestamp != currentShotOnGoalTimestamp)
			eventClass = AttackResultPredictionInstance.CLASS_SHOT_ON_GOAL;

		if (!eventClass.equals("")) {
			train(gameInformation, eventClass);

			// reset long tracked values
			passCounter = 0;
			velocityHistory.clear();
			lastGameTime = -1;
			lastBallYPosition = -1;
		} else
			predict(gameInformation);

		// save values
		idOfLastPlayerWithBall = idOfCurrentPlayerWithBall;

		lastBallLossTimestamp = currentBallLossTimestamp;
		lastBallOutsideTimestamp = currentBallOutsideTimestamp;
		lastShotOnGoalTimestamp = currentShotOnGoalTimestamp;

	}

	@Override
	protected void predict(GameInformation gameInformation) {
		learner.makePrediction(predictionInstance);
	}

	@Override
	protected void train(GameInformation gameInformation, String classAttribute) {

		// count result for comparison with prediction accuracy
		if (classAttribute
				.equals(AttackResultPredictionInstance.CLASS_BALL_LOSS)) {
			ballLossCounter++;
		} else {
			noBallLossCounter++;
		}

		if (Utils.DEBUGGING)
			System.out.println(TAG + "event occured: " + classAttribute);

		((AttackResultPredictionInstance) predictionInstance)
				.setClassAttribute(classAttribute);

		learner.train(predictionInstance);

		System.out
				.println(TAG
						+ "Ball loss rate = "
						+ ((float) ballLossCounter
								/ ((float) ballLossCounter + (float) noBallLossCounter) * 100)
						+ "%");

	}

}
