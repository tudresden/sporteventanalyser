package predictions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.core.GameInformation;

public class Prophet {
	public static final String TAG = "[Predictions][Prophet] ";

	// time between prediction updates TODO should be game time not system time
	private static final long PREDICTIONS_PERIOD_TIME = 200;

	private List<Predictor> listOfPredictors;
	private GameInformation gameInformation;
	private Timer timer;

	public Prophet(GameInformation gameInformation) {
		this.gameInformation = gameInformation;

		listOfPredictors = new ArrayList<Predictor>();

		// add predictors and the learner for the predictors

		// listOfPredictors.add(new PassSuccessPredictor(
		// new HoeffdingTreeLearner()));

		listOfPredictors.add(new PassSuccessPredictor(new IbkLearner()));

		// listOfPredictors.add(new PassSuccessPredictor(new knnLearner()));
	}

	/**
	 * Start the periodic predictions.
	 */
	public void start() {
		System.out.println(TAG + "Prophet awakened.");

		if (timer != null)
			timer.cancel();

		timer = new Timer();

		timer.schedule(new TimerTask() {
			public void run() {
				updatePredictors();
			}
		}, 0, PREDICTIONS_PERIOD_TIME);
	}

	/**
	 * Stop the periodic predictions.
	 */
	public void stop() {
		if (timer != null)
			timer.cancel();
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
