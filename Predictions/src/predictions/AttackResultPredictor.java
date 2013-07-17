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
	/**
	 * Tag for logs.
	 */
	public static final String TAG = "[Predictions][AttackResultPredictor] ";

	/**
	 * Specifies the radius in which players will be counted.
	 */
	private static final int PLAYER_RADIUS = 20;
	/**
	 * Specifies the number of instances to write to the ARFF file.
	 */
	private static final int NUMBER_OF_INSTANCES_FOR_ARFF = 350; // 352instances

	/**
	 * Stores the id of the last player with ball.
	 */
	private int idOfLastPlayerWithBall = -1;

	/**
	 * Counts all passes occurred.
	 */
	private int passCounter = 0;

	/**
	 * Remembers if ARFF file has already been created.
	 */
	private boolean arffCreated = false;

	/**
	 * Saves the ball velocity values of the past seconds.
	 */
	private List<Integer> velocityHistory = new LinkedList<Integer>();
	/**
	 * Stores the last game time.
	 */
	private long lastGameTime = -1;
	/**
	 * Stores the last y position of the ball.
	 */
	private int lastBallYPosition = -1;

	/**
	 * Stores the time stamp of last ball loss to notice a new loss of ball.
	 */
	private long lastBallLossTimestamp = -1;
	/**
	 * Stores the time stamp of last ball out of bounds to notice a new
	 * occurrence of ball out of bounds.
	 */
	private long lastBallOutsideTimestamp = -1;
	/**
	 * Stores the time stamp of last shot on goal to notice a new occurrence of
	 * shot on goal.
	 */
	private long lastShotOnGoalTimestamp = -1;

	/**
	 * Counts all ball losses for distribution calculations.
	 */
	private int ballLossCounter = 0;
	/**
	 * Counts all shots on goal for distribution calculations.
	 */
	private int shotOnGoalCounter = 0;
	/**
	 * Counts all balls out of bounds for distribution calculations.
	 */
	private int ballOutOfBoundsCounter = 0;

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

	@SuppressWarnings("unused")
	@Override
	public void update(GameInformation gameInformation) {

		if (Utils.WRITE_DEBUGGING_LOGS)
			System.out.println(TAG
					+ " - - - attack result prediction update with "
					+ learner.getClass().getName() + " - - - ");

		if (Utils.ARFF_WRITING_MODE && !arffCreated)
			if (learner.getAccumulatedInstances().size() == NUMBER_OF_INSTANCES_FOR_ARFF) {
				arffCreated = true;
				Utils.createArffFileFromInstances(learner
						.getAccumulatedInstances(), this.getClass().getName()
						+ "_" + predictionInstance.getClass().getName());
			}

		if (gameInformation.getCurrentBallPossessionPlayer() == null) {
			if (Utils.WRITE_DEBUGGING_LOGS)
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
		int currentBallYPosition = gameInformation
				.getCurrentBallPossessionPlayer().getPositionY();

		if (lastGameTime == -1) {
			lastBallYPosition = gameInformation
					.getCurrentBallPossessionPlayer().getPositionY();
			lastGameTime = gameInformation.getCurrentGameTime();
		}

		// calculate velocity
		int velocity = (int) (Math
				.abs(currentBallYPosition - lastBallYPosition) / (currentGameTime
				- lastGameTime + 1));
		velocityHistory.add(velocity);
		if (velocityHistory.size() > 8)
			velocityHistory.remove(0);
		int averageVelocity = 0;
		for (int velocityValue : velocityHistory)
			averageVelocity += velocityValue / velocityHistory.size();

		lastBallYPosition = currentBallYPosition;
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
						passCounter, (int) gameInformation
								.getDistanceOfNearestOpponent(),
						averageVelocity);

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
		float[] predictionsBundle = learner.makePrediction(predictionInstance);

		if (Utils.WRITE_DEBUGGING_LOGS)
			System.out.println("PREDICTION" + "  loss: " + predictionsBundle[0]
					+ "%  out of bounds: " + predictionsBundle[1]
					+ "%  shot on goal: " + predictionsBundle[2] + "%");

		// send to visualization
		if (gameInformation.getStatisticsFacade() != null)
			gameInformation.getStatisticsFacade().setAttackResultPrediction(
					predictionsBundle[1], predictionsBundle[0],
					predictionsBundle[2]);
	}

	@Override
	protected void train(GameInformation gameInformation, String classAttribute) {

		// count result for comparison with prediction accuracy
		if (classAttribute
				.equals(AttackResultPredictionInstance.CLASS_BALL_LOSS)) {
			ballLossCounter++;
		} else if (classAttribute
				.equals(AttackResultPredictionInstance.CLASS_BALL_OUT_OF_BOUNDS)) {
			ballOutOfBoundsCounter++;
		} else {
			shotOnGoalCounter++;
		}

		if (Utils.WRITE_DEBUGGING_LOGS)
			System.out.println(TAG + "event occured: " + classAttribute);

		((AttackResultPredictionInstance) predictionInstance)
				.setClassAttribute(classAttribute);

		learner.train(predictionInstance);

		if (Utils.WRITE_INFO_LOGS)
			System.out
					.println(TAG
							+ "Ball loss = "
							+ ((float) ballLossCounter
									/ ((float) ballLossCounter
											+ (float) shotOnGoalCounter + (float) ballOutOfBoundsCounter) * 100)
							+ "%"
							+ "  Ball out of bounds = "
							+ ((float) ballOutOfBoundsCounter
									/ ((float) ballLossCounter
											+ (float) shotOnGoalCounter + (float) ballOutOfBoundsCounter) * 100)
							+ "%"
							+ "  Shot on goal = "
							+ ((float) shotOnGoalCounter
									/ ((float) ballLossCounter
											+ (float) shotOnGoalCounter + (float) ballOutOfBoundsCounter) * 100)
							+ "%");

	}

}
