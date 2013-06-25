package experimental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moa.core.InstancesHeader;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

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

	private InstancesHeader instanceHeader;
	private Instance currentInstance;

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

		// create header for learner
		this.instanceHeader = new InstancesHeader(new Instances(this.getClass()
				.getName(), attributes, 0));
		this.instanceHeader
				.setClassIndex(this.instanceHeader.numAttributes() - 1);

		createEmptyInstance();

	}

	private void createEmptyInstance() {
		currentInstance = new DenseInstance(getHeader().numAttributes());

		for (int playerIndex = 0; playerIndex < playerNames.size(); playerIndex++)
			currentInstance.setValue(playerIndex, 0);

		currentInstance.setDataset(getHeader());
		// currentInstance.setClassValue(EVENT_GOAL);
	}

	public Instance getTrainingInstance(String result) {
		Instance instanceForLearner = (Instance) currentInstance.copy();
		instanceForLearner.setClassValue(result);
		createEmptyInstance();
		return instanceForLearner;
	}

	public InstancesHeader getHeader() {
		return this.instanceHeader;
	}

	public Instance getInstanceForPrediction() {
		return currentInstance;
	}

	public void updateInstance(String playerName) {
		int playerIndex = playerNames.indexOf(playerName);
		currentInstance.setValue(playerIndex,
				currentInstance.value(playerIndex) + 1);
	}

}
