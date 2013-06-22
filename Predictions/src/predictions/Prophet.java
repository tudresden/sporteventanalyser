package predictions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.core.GameInformation;

public class Prophet {
	private static final long PREDICTIONS_PERIOD_TIME = 3 * 1000;

	private List<Predictor> listOfPredictors;
	private GameInformation gameInformation;
	private Timer timer;

	public Prophet(GameInformation gameInformation) {
		this.gameInformation = gameInformation;

		listOfPredictors = new ArrayList<Predictor>();
		listOfPredictors.add(new PassSuccessPredictor());
	}

	/**
	 * Start the periodic predictions.
	 */
	public void start() {
		System.out.println("Prophet awakened.");

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

	private void updatePredictors() {
		System.out.println("Updating predictors.");

		for (Predictor predictor : listOfPredictors)
			predictor.update(gameInformation);
	}
}
