package predictions;

import java.util.ArrayList;
import java.util.List;

import de.core.GameInformation;

/**
 * The prophet predicts the future. Bless the prophet!
 * 
 */
public class Prophet {
	/**
	 * Tag for logs.
	 */
	public static final String TAG = "[Predictions][Prophet] ";

	/**
	 * Contains all predictors, which has to be updated periodically.
	 */
	private List<Predictor> listOfPredictors;
	/**
	 * Reference to the statistics.
	 */
	private GameInformation gameInformation;

	/**
	 * Initiates the prophet.
	 * 
	 * @param gameInformation
	 *            a reference to the game statistics
	 */
	public Prophet(GameInformation gameInformation) {
		this.gameInformation = gameInformation;

		listOfPredictors = new ArrayList<Predictor>();

		// add predictors and the learner for the predictors

		/*
		 * pass prediction
		 */

		listOfPredictors.add(new PassSuccessPredictor(new IbkLearner()));

		/*
		 * attack result prediction
		 */

		listOfPredictors.add(new AttackResultPredictor(new IbkLearner(2)));

	}

	/**
	 * Updates predictions.
	 */
	public void updatePredictors() {
		if (Utils.WRITE_DEBUGGING_LOGS)
			System.out.println(TAG + "Updating predictors.");

		for (Predictor predictor : listOfPredictors)
			predictor.update(gameInformation);
	}
}
