package predictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moa.core.InstancesHeader;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class PassSuccessPredictionInstance extends PredictionInstance {

	public static final String PREDICTION_PASS_SUCCESSFUL = "PREDICTION_PASS_SUCCESSFUL";
	public static final String PREDICTION_PASS_FAILS = "PREDICTION_PASS_FAILS";

	public static final String ATTRIBUTE_NUMBER_OF_TEAMMATES_IN_AREA = "ATTRIBUTE_NUMBER_OF_TEAMMATES_IN_AREA";
	public static final String ATTRIBUTE_NUMBER_OF_OPPONENTS_IN_AREA = "ATTRIBUTE_NUMBER_OF_OPPONENTS_IN_AREA";
	public static final String ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE = "ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE";
	public static final String ATTRIBUTE_CLASS = "class";

	private static final List<String> ATTRIBUTE_LIST = Arrays
			.asList(new String[] { ATTRIBUTE_NUMBER_OF_TEAMMATES_IN_AREA,
					ATTRIBUTE_NUMBER_OF_OPPONENTS_IN_AREA,
					ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE,
					ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE, ATTRIBUTE_CLASS

			});

	private InstancesHeader instanceHeader;
	private Instance currentInstance;

	public PassSuccessPredictionInstance() {
		init();
	}

	@Override
	public void init() {
		// define attributes
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();

		attributes.add(new Attribute(ATTRIBUTE_NUMBER_OF_TEAMMATES_IN_AREA));
		attributes.add(new Attribute(ATTRIBUTE_NUMBER_OF_OPPONENTS_IN_AREA));
		attributes.add(new Attribute(ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE));

		ArrayList<String> classLabels = new ArrayList<String>();
		classLabels.add(PREDICTION_PASS_SUCCESSFUL);
		classLabels.add(PREDICTION_PASS_FAILS);
		attributes.add(new Attribute(ATTRIBUTE_CLASS, classLabels));

		// create header for learner
		this.instanceHeader = new InstancesHeader(new Instances(this.getClass()
				.getName(), attributes, 0));
		this.instanceHeader
				.setClassIndex(this.instanceHeader.numAttributes() - 1);

		createEmptyInstance();
	}

	private void createEmptyInstance() {
		currentInstance = new DenseInstance(getHeader().numAttributes());
		currentInstance.setDataset(getHeader());
	}

	@Override
	public InstancesHeader getHeader() {
		return this.instanceHeader;
	}

	@Override
	public Instance getInstance() {
		return currentInstance;
	}

	@Override
	public Instance getInstanceCopy() {
		return (Instance) currentInstance.copy();
	}

	public void setAttributes(int numberOfTeammatesInArea,
			int numberOfOpponentsInArea, int playerPassSuccessRate) {
		createEmptyInstance();

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_NUMBER_OF_TEAMMATES_IN_AREA),
				numberOfTeammatesInArea);
		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_NUMBER_OF_OPPONENTS_IN_AREA),
				numberOfOpponentsInArea);
		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE),
				playerPassSuccessRate);
	}

	public void setClassAttribute(String result) {
		currentInstance.setClassValue(result);
	}

}
