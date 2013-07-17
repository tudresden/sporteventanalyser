package predictions;

import de.core.GameInformation;

/**
 * Calculates the probability of the success of the next pass.
 * 
 */
public class PassSuccessPredictor extends Predictor {
	/**
	 * Tag for logs.
	 */
	public static final String TAG = "[Predictions][PassSuccessPredictor] ";

	/**
	 * Specifies the radius in which players will be counted.
	 */
	private static final int PLAYER_RADIUS = 20;
	/**
	 * Specifies the number of instances to write to the ARFF file.
	 */
	private static final int NUMBER_OF_INSTANCES_FOR_ARFF = 820; // 827instances
	/**
	 * Stores the id of the last player with ball.
	 */
	private int idOfLastPlayerWithBall = -1;

	/**
	 * Counts all successful passes for distribution calculations.
	 */
	private int successfulPassesCounter = 0;
	/**
	 * Counts all failed passes for distribution calculations.
	 */
	private int failedPassesCounter = 0;

	/**
	 * Remembers if ARFF file has already been created.
	 */
	private boolean arffCreated = false;

	/**
	 * Instantiates the pass success predictor.
	 * 
	 * @param learner
	 *            the classifier to use for prediction
	 */
	public PassSuccessPredictor(Learner learner) {
		super(learner);
	}

	@Override
	public void init() {
		predictionInstance = new PassSuccessPredictionInstance();
		learner.init(predictionInstance.getHeader());
	}

	@SuppressWarnings("unused")
	@Override
	public void update(GameInformation gameInformation) {

		if (Utils.WRITE_DEBUGGING_LOGS)
			System.out.println(TAG
					+ " - - - pass success prediction update with "
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

		// update instance
		((PassSuccessPredictionInstance) predictionInstance)
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
						(int) gameInformation.getDistanceOfNearestOpponent(),
						gameInformation.getCurrentBallPossessionPlayer()
								.getPositionX(), gameInformation
								.getCurrentBallPossessionPlayer()
								.getPositionY(), Math.round(gameInformation
								.getPlayerDistance(idOfLastPlayerWithBall)),
						gameInformation.isPlayerOnOwnSide(gameInformation
								.getCurrentBallPossessionPlayer()));

		// pass occurred
		if (idOfCurrentPlayerWithBall != idOfLastPlayerWithBall
				&& idOfLastPlayerWithBall != -1) {
			if (Utils.WRITE_DEBUGGING_LOGS)
				System.out.println(TAG + "A pass occured.");
			train(gameInformation, "");
		}
		// no pass occurred
		else {
			if (Utils.WRITE_DEBUGGING_LOGS)
				System.out.println(TAG + "No pass occured.");
			predict(gameInformation);
		}

		idOfLastPlayerWithBall = gameInformation
				.getCurrentBallPossessionPlayer().getId();

	}

	@Override
	protected void predict(GameInformation gameInformation) {
		float[] predictionsBundle = learner.makePrediction(predictionInstance);

		if (Utils.WRITE_DEBUGGING_LOGS)
			System.out.println("PREDICTION" + "  pass success: "
					+ predictionsBundle[0] + "%  pass fail: "
					+ predictionsBundle[1] + "%");

		// send to visualization
		if (gameInformation.getStatisticsFacade() != null)
			gameInformation.getStatisticsFacade().setPassSuccessPrediction(
					predictionsBundle[0]);
	}

	@Override
	protected void train(GameInformation gameInformation, String classAttribute) {

		String result = gameInformation
				.isPlayerLastPassSuccessful(idOfLastPlayerWithBall) ? PassSuccessPredictionInstance.CLASS_PASS_SUCCESSFUL
				: PassSuccessPredictionInstance.CLASS_PASS_FAILS;

		// count result for comparison with prediction accuracy
		if (result.equals(PassSuccessPredictionInstance.CLASS_PASS_FAILS)) {
			failedPassesCounter++;
		} else {
			successfulPassesCounter++;
		}

		if (Utils.WRITE_DEBUGGING_LOGS)
			System.out.println(TAG + "event occured: " + result);

		((PassSuccessPredictionInstance) predictionInstance)
				.setClassAttribute(result);

		learner.train(predictionInstance);

		if (Utils.WRITE_INFO_LOGS)
			System.out
					.println(TAG
							+ "Successful passes rate = "
							+ ((float) successfulPassesCounter
									/ ((float) successfulPassesCounter + (float) failedPassesCounter) * 100)
							+ "%");

	}

}
