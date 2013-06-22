package predictions;

import java.util.ArrayList;
import java.util.List;

import de.core.GameInformation;

public class Prophet {
	private List<Predictor> listOfPredictors;
	private GameInformation gameInformation;

	public Prophet(GameInformation gameInformation) {
		this.gameInformation = gameInformation;

		listOfPredictors = new ArrayList<Predictor>();
	}

	public void start() {
		System.out.println("Prophet awakened.");
	}

	public void stop() {

	}
}
