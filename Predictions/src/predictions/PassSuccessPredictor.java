package predictions;

import de.core.GameInformation;

/**
 * Calculates the probability of the success of the next pass.
 * 
 */
public class PassSuccessPredictor extends Predictor {

	public static final String TAG = "[Predictions][PassSuccessPredictor] ";

	private static final int PLAYER_RADIUS = 20;

	private int idOfLastPlayerWithBall = -1;

	private int successfulPassesCounter = 0;
	private int failedPassesCounter = 0;

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

		// learner = new KnnLearner(predictionInstance.getHeader());
		// learner = new HoeffdingTreeLearner(predictionInstance.getHeader());
		// learner = new IbkLearner(predictionInstance.getHeader());

		learner.init(predictionInstance.getHeader());
	}

	@Override
	public void update(GameInformation gameInformation) {

		System.out.println(TAG + " - - - pass success prediction update with "
				+ learner.getClass().getName() + " - - - ");

		// TODO create ARFF file at the very end
		if (Utils.ARFF_WRITING_MODE && !arffCreated)
			if (learner.getAccumulatedInstances().size() == 760) {
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

		// System.out.println(TAG + "Last player: " + idOfLastPlayerWithBall
		// + " Current player:" + idOfCurrentPlayerWithBall);

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
			System.out.println(TAG + "A pass occured.");
			train(gameInformation);
		}
		// no pass occurred
		else {

			System.out.println(TAG + "No pass occured.");
			predict(gameInformation);
		}

		idOfLastPlayerWithBall = gameInformation
				.getCurrentBallPossessionPlayer().getId();

	}

	@Override
	protected void predict(GameInformation gameInformation) {
		learner.makePrediction(predictionInstance);
	}

	@Override
	protected void train(GameInformation gameInformation) {

		String result = gameInformation
				.isPlayerLastPassSuccessful(idOfLastPlayerWithBall) ? PassSuccessPredictionInstance.CLASS_PASS_SUCCESSFUL
				: PassSuccessPredictionInstance.CLASS_PASS_FAILS;

		// count result for comparison with prediction accuracy
		if (result.equals(PassSuccessPredictionInstance.CLASS_PASS_FAILS)) {
			failedPassesCounter++;
			// utils.logInt(new Integer[] {
			// gameInformation.getTeammatesInArea(PLAYER_RADIUS),
			// gameInformation.getOpponentsInArea(PLAYER_RADIUS),
			// gameInformation
			// .getPlayerPassesSuccessful(idOfLastPlayerWithBall),
			// gameInformation
			// .getPlayerPassesMissed(idOfLastPlayerWithBall),
			// gameInformation
			// .getPlayerBallContacts(idOfLastPlayerWithBall),
			// idOfLastPlayerWithBall,
			// gameInformation.getCurrentBallPossessionPlayer().getId(),
			// (int) gameInformation.getDistanceOfNearestTeammate(),
			// gameInformation.getCurrentBallPossessionPlayer()
			// .getPositionX(),
			// gameInformation.getCurrentBallPossessionPlayer()
			// .getPositionY(),
			//
			// (int) gameInformation.getCurrentGameTime(),
			// Math.round(gameInformation
			// .getPlayerDistance(idOfLastPlayerWithBall)),
			// (int) gameInformation
			// .getPlayerLastPassTimestamp(idOfLastPlayerWithBall),
			// 0 });
		} else {
			successfulPassesCounter++;
			// utils.logInt(new Integer[] {
			// gameInformation.getTeammatesInArea(PLAYER_RADIUS),
			// gameInformation.getOpponentsInArea(PLAYER_RADIUS),
			// gameInformation
			// .getPlayerPassesSuccessful(idOfLastPlayerWithBall),
			// gameInformation
			// .getPlayerPassesMissed(idOfLastPlayerWithBall),
			// gameInformation
			// .getPlayerBallContacts(idOfLastPlayerWithBall),
			// idOfLastPlayerWithBall,
			// gameInformation.getCurrentBallPossessionPlayer().getId(),
			// (int) gameInformation.getDistanceOfNearestTeammate(),
			// gameInformation.getCurrentBallPossessionPlayer()
			// .getPositionX(),
			// gameInformation.getCurrentBallPossessionPlayer()
			// .getPositionY(),
			//
			// (int) gameInformation.getCurrentGameTime(),
			// Math.round(gameInformation
			// .getPlayerDistance(idOfLastPlayerWithBall)),
			// (int) gameInformation
			// .getPlayerLastPassTimestamp(idOfLastPlayerWithBall),
			// 1 });
		}

		if (Utils.DEBUGGING)
			System.out.println(TAG + "event occured: " + result);

		((PassSuccessPredictionInstance) predictionInstance)
				.setClassAttribute(result);

		learner.train(predictionInstance);

		// Utils.logArff(predictionInstance.getInstance());

		System.out
				.println(TAG
						+ "Successful passes rate = "
						+ ((float) successfulPassesCounter
								/ ((float) successfulPassesCounter + (float) failedPassesCounter) * 100)
						+ "%");

	}

}
