package predictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weka.core.Attribute;

public class GoalPredictionInstanceManager {
	public static final String EVENT_GOAL = "goal";
	public static final String EVENT_BALL_LOSS = "loss";
	public static final String EVENT_BALL_OUT_OF_BOUNDS = "out of bounds";

	public static final List<String> playerNames = Arrays.asList(

	"Nick Gertje", "Dennis Dotterweich", "Niklas Waelzlein", "Wili Sommer",
			"Philipp Harlass", "Roman Hartleb", "Erik Engelhardt",
			"Sandro Schneider",

			"Leon Krapf", "Kevin Baer", "Luca Ziegler", "Ben Mueller",
			"Vale Reitstetter", "Christopher Lee", "Leon Heinze",
			"Leo Langhans");

	public GoalPredictionInstanceManager() {
		init();
	}

	private void init() {

		// define attributes
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();

		for (String player : playerNames) {
			Attribute playerAttribute = new Attribute(player);
			attributes.add(playerAttribute);
		}

		ArrayList<String> classLabels = new ArrayList<String>();
		classLabels.add(EVENT_GOAL);
		classLabels.add(EVENT_BALL_LOSS);
		classLabels.add(EVENT_BALL_OUT_OF_BOUNDS);
		attributes.add(new Attribute("class", classLabels));

	}

}
