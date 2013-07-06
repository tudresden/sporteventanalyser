package predictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moa.core.InstancesHeader;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Encapsulates an instance for training and pass success prediction.
 * 
 */
public class PassSuccessPredictionInstance extends PredictionInstance {

	public static final String CLASS_PASS_SUCCESSFUL = "CLASS_PASS_SUCCESSFUL";
	public static final String CLASS_PASS_FAILS = "CLASS_PASS_FAILS";

	// public static final String ATTRIBUTE_TEAMMATE_RATE_IN_AREA =
	// "ATTRIBUTE_TEAMMATE_RATE_IN_AREA";
	// public static final String ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE =
	// "ATTRIBUTE_PLAYER_PASS_SUCCESS_RATE";

	private static final String ATTRIBUTE_TEAMMATE_IN_AREA = "ATTRIBUTE_TEAMMATE_IN_AREA";
	private static final String ATTRIBUTE_OPPPOSITE_IN_AREA = "ATTRIBUTE_OPPPOSITE_IN_AREA";
	private static final String ATTRIBUTE_PLAYER_PASS_SUCCESS = "ATTRIBUTE_PLAYER_PASS_SUCCESS";
	private static final String ATTRIBUTE_PLAYER_PASS_MISSED = "ATTRIBUTE_PLAYER_PASS_MISSED";
	private static final String ATTRIBUTE_PLAYER_BALLCONTACT = "ATTRIBUTE_PLAYER_BALLCONTACT";
	private static final String ATTRIBUTE_LAST_PLAYER_ID = "ATTRIBUTE_LAST_PLAYER_ID";
	private static final String ATTRIBUTE_CURRENT_PLAYER_ID = "ATTRIBUTE_CURRENT_PLAYER_ID";
	private static final String ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER = "ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER";
	private static final String ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT = "ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT";
	private static final String ATTRIBUTE_CURRENT_PLAYER_X = "ATTRIBUTE_CURRENT_PLAYER_X";
	private static final String ATTRIBUTE_CURRENT_PLAYER_Y = "ATTRIBUTE_CURRENT_PLAYER_Y";
	private static final String ATTRIBUTE_CURRENT_PLAYER_DISTANCE = "ATTRIBUTE_CURRENT_PLAYER_DISTANCE";
	private static final String ATTRIBUTE_AREA = "ATTRIBUTE_AREA";

	public static final String ATTRIBUTE_CLASS = "PassSuccessPrediction";

	private static final List<String> ATTRIBUTE_LIST = Arrays
			.asList(new String[] { ATTRIBUTE_TEAMMATE_IN_AREA,
					ATTRIBUTE_OPPPOSITE_IN_AREA, ATTRIBUTE_PLAYER_PASS_SUCCESS,
					ATTRIBUTE_PLAYER_PASS_MISSED, ATTRIBUTE_PLAYER_BALLCONTACT,
					ATTRIBUTE_LAST_PLAYER_ID, ATTRIBUTE_CURRENT_PLAYER_ID,
					ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER,ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT,
					ATTRIBUTE_CURRENT_PLAYER_X, ATTRIBUTE_CURRENT_PLAYER_Y,
					ATTRIBUTE_CURRENT_PLAYER_DISTANCE, ATTRIBUTE_AREA,
					ATTRIBUTE_CLASS });

	private InstancesHeader instanceHeader;
	private Instance currentInstance;
	ArrayList<String> players;

	/**
	 * Instantiates the prediction instance and initializes it.
	 */
	public PassSuccessPredictionInstance() {
		super();
	}

	@Override
	public void init() {

		/*
		 * attributes
		 */
		 //Player identify with ID
		players = new ArrayList<String>();
		players.add("47");
		players.add("49");
		players.add("19");
		players.add("53");
		players.add("23");
		players.add("57");
		players.add("59");
		players.add("63");
		players.add("65");
		players.add("67");
		players.add("69");
		players.add("71");
		players.add("73");
		players.add("75");

		//create Attribute-ArrayList for Instances
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();

		attributes.add(new Attribute(ATTRIBUTE_TEAMMATE_IN_AREA));

		attributes.add(new Attribute(ATTRIBUTE_OPPPOSITE_IN_AREA));

		attributes.add(new Attribute(ATTRIBUTE_PLAYER_PASS_SUCCESS));

		attributes.add(new Attribute(ATTRIBUTE_PLAYER_PASS_MISSED));

		attributes.add(new Attribute(ATTRIBUTE_PLAYER_BALLCONTACT));

		attributes.add(new Attribute(ATTRIBUTE_LAST_PLAYER_ID, players));

		attributes.add(new Attribute(ATTRIBUTE_CURRENT_PLAYER_ID, players));

		attributes.add(new Attribute(ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER));
		
		attributes.add(new Attribute(ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT));

		attributes.add(new Attribute(ATTRIBUTE_CURRENT_PLAYER_X));

		attributes.add(new Attribute(ATTRIBUTE_CURRENT_PLAYER_Y));

		attributes.add(new Attribute(ATTRIBUTE_CURRENT_PLAYER_DISTANCE));

		// field area
		ArrayList<String> areas = new ArrayList<String>();
		areas.add(Utils.FIELD_AREA_OWN_TEAM);
		areas.add(Utils.FIELD_AREA_MIDDLE);
		areas.add(Utils.FIELD_AREA_OPPONENTS);
		attributes.add(new Attribute(ATTRIBUTE_AREA, areas));

		/*
		 * classes
		 */

		// pass successful or fail
		ArrayList<String> classLabels = new ArrayList<String>();
		classLabels.add(CLASS_PASS_SUCCESSFUL);
		classLabels.add(CLASS_PASS_FAILS);
		attributes.add(new Attribute(ATTRIBUTE_CLASS, classLabels));

		/*
		 * create header for learner
		 */

		this.instanceHeader = new InstancesHeader(new Instances(this.getClass()
				.getName(), attributes, 10));
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

	/**
	 * Sets the attributes of the instance. No class is set.
	 * 
	 * @param numberOfTeammatesInArea
	 * @param numberOfOpponentsInArea
	 * @param playerPassesSuccessful
	 * @param playerPassesMissed
	 * @param ballContact
	 * @param lastPlayerId
	 * @param curentPlayerId
	 * @param distanceNearestPlayer
	 * @param currentX
	 * @param currentY
	 * @param playerDistance
	 * @param playerOnOwnSide
	 */
	public void setAttributes(int numberOfTeammatesInArea,
			int numberOfOpponentsInArea, int playerPassesSuccessful,
			int playerPassesMissed, int ballContact, String lastPlayerId,
			String curentPlayerId, int distanceNearestPlayer, int distanceNearestOpponent, int currentX,
			int currentY, int playerDistance, boolean playerOnOwnSide) {
		createEmptyInstance();

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_TEAMMATE_IN_AREA),
				numberOfTeammatesInArea);
		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_OPPPOSITE_IN_AREA),
				numberOfOpponentsInArea);

		int playerPassSuccessRate = (int) (((float) playerPassesSuccessful / ((float) playerPassesSuccessful + (float) playerPassesMissed)) * 100);
		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_PLAYER_PASS_SUCCESS),
				playerPassSuccessRate);

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_PLAYER_PASS_MISSED), 0);

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_PLAYER_BALLCONTACT),
				ballContact);

		if (players.contains(lastPlayerId))
			currentInstance.setValue(
					ATTRIBUTE_LIST.indexOf(ATTRIBUTE_LAST_PLAYER_ID),
					lastPlayerId);

		if (players.contains(curentPlayerId))
			currentInstance.setValue(
					ATTRIBUTE_LIST.indexOf(ATTRIBUTE_CURRENT_PLAYER_ID),
					curentPlayerId);

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER),
				distanceNearestPlayer);

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_CURRENT_PLAYER_X), currentX);

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT),
				distanceNearestOpponent);
				
		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_CURRENT_PLAYER_Y), currentY);

		currentInstance.setValue(ATTRIBUTE_LIST.indexOf(ATTRIBUTE_AREA),
				Utils.getFieldArea(currentY, !playerOnOwnSide));

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_CURRENT_PLAYER_DISTANCE),
				playerDistance);
	}

	/**
	 * Sets the class attribute.
	 * 
	 * @param result
	 *            the class
	 */
	public void setClassAttribute(String result) {
		currentInstance.setClassValue(result);
	}

}
