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

	public static final String ATTRIBUTE_TEAMMATE_RATE_IN_AREA = "ATTRIBUTE_TEAMMATE_RATE_IN_AREA";
	public static final String ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE = "ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE";
	public static final String ATTRIBUTE_AREA = "ATTRIBUTE_AREA";
	public static final String ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER = "ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER";

	public static final String ATTRIBUTE_CLASS = "class";

	private static final List<String> ATTRIBUTE_LIST = Arrays
			.asList(new String[] { ATTRIBUTE_TEAMMATE_RATE_IN_AREA,
					ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE, ATTRIBUTE_AREA,
					ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER, ATTRIBUTE_CLASS

			});

	private InstancesHeader instanceHeader;
	private Instance currentInstance;

	public PassSuccessPredictionInstance() {
		init();
	}

	@Override
	public void init() {

		/*
		 * attributes
		 */

		ArrayList<Attribute> attributes = new ArrayList<Attribute>();

		// number of team mates minus opponents in area
		attributes.add(new Attribute(ATTRIBUTE_TEAMMATE_RATE_IN_AREA));

		// player pass success rate
		attributes.add(new Attribute(ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE));

		// field area
		ArrayList<String> areas = new ArrayList<String>();
		areas.add(Utils.FIELD_AREA_OWN_TEAM);
		areas.add(Utils.FIELD_AREA_MIDDLE);
		areas.add(Utils.FIELD_AREA_OPPONENTS);
		attributes.add(new Attribute(ATTRIBUTE_AREA, areas));

		// distance to nearest friendly player
		attributes.add(new Attribute(ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER));

		/*
		 * classes
		 */

		// pass successful or fail
		ArrayList<String> classLabels = new ArrayList<String>();
		classLabels.add(PREDICTION_PASS_SUCCESSFUL);
		classLabels.add(PREDICTION_PASS_FAILS);
		attributes.add(new Attribute(ATTRIBUTE_CLASS, classLabels));

		/*
		 * create header for learner
		 */

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
			int numberOfOpponentsInArea, int playerPassesSuccessful,
			int playerPassesMissed, int xPosition, int distanceNearestPlayer) {
		createEmptyInstance();

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_TEAMMATE_RATE_IN_AREA),
				numberOfTeammatesInArea - numberOfOpponentsInArea);

		int playerPassSuccessRate = (int) (((float) playerPassesSuccessful / ((float) playerPassesSuccessful + (float) playerPassesMissed)) * 100);
		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE),
				playerPassSuccessRate);

		currentInstance.setValue(ATTRIBUTE_LIST.indexOf(ATTRIBUTE_AREA),
				Utils.getFieldArea(xPosition, true)); // TODO set opponentSide

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER),
				distanceNearestPlayer);
	}

	public void setClassAttribute(String result) {
		currentInstance.setClassValue(result);
	}

}
