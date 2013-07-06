package predictions;

import java.util.ArrayList;
import java.util.List;

import de.core.GameInformation;

/**
 * The prophet predicts the future. Bless the prophet!
 * 
 */
public class Prophet {
	public static final String TAG = "[Predictions][Prophet] ";

	private List<Predictor> listOfPredictors;
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

		// listOfPredictors.add(new PassSuccessPredictor(
		// new HoeffdingTreeLearner()));
		listOfPredictors.add(new PassSuccessPredictor(new IbkLearner()));

		// listOfPredictors.add(new PassSuccessPredictor(new knnLearner()));

		/*
		 * attack result prediction
		 */

		// listOfPredictors.add(new AttackResultPredictor(
		// new HoeffdingTreeLearner()));
		listOfPredictors.add(new AttackResultPredictor(new IbkLearner()));
	}

	/**
	 * Updates predictions.
	 */
	public void updatePredictors() {
		// System.out.println(TAG + "Updating predictors.");

		for (Predictor predictor : listOfPredictors)
			predictor.update(gameInformation);
	}
}
