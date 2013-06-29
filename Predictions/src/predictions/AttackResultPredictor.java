package predictions;

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

	private int ballLossCounter = 0;
	private int noBallLossCounter = 0;
	private int passCounter = 0;

	private boolean arffCreated = false;

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
			if (learner.getAccumulatedInstances().size() == 50) {
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
										.getId(), gameInformation
								.getCurrentBallPossessionPlayer()
								.getPositionX(), gameInformation
								.getCurrentBallPossessionPlayer()
								.getPositionY(), (int) gameInformation
								.getDistanceOfNearestTeammate(),
						Math.round(gameInformation
								.getPlayerDistance(idOfLastPlayerWithBall)),
						gameInformation.isPlayerOnOwnSide(gameInformation
								.getCurrentBallPossessionPlayer()), passCounter);

		// class event occurred TODO check if class event occurred
		boolean train = false;
		if (train) {
			train(gameInformation);
			passCounter = 0;
		} else
			predict(gameInformation);

		idOfLastPlayerWithBall = gameInformation
				.getCurrentBallPossessionPlayer().getId();

	}

	@Override
	protected void predict(GameInformation gameInformation) {
		learner.makePrediction(predictionInstance);
	}

	@Override
	protected void train(GameInformation gameInformation) {

		// TODO determine class
		String result = AttackResultPredictionInstance.CLASS_BALL_LOSS;

		// count result for comparison with prediction accuracy
		if (result.equals(AttackResultPredictionInstance.CLASS_BALL_LOSS)) {
			ballLossCounter++;
		} else {
			noBallLossCounter++;
		}

		if (Utils.DEBUGGING)
			System.out.println(TAG + "event occured: " + result);

		((AttackResultPredictionInstance) predictionInstance)
				.setClassAttribute(result);

		learner.train(predictionInstance);

		System.out
				.println(TAG
						+ "Ball loss rate = "
						+ ((float) ballLossCounter
								/ ((float) ballLossCounter + (float) noBallLossCounter) * 100)
						+ "%");

	}

}
