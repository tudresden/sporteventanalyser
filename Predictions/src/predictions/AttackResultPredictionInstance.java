package predictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moa.core.InstancesHeader;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.AveragingResultProducer;

/**
 * Encapsulates an instance for training and attack result prediction.
 * 
 */
public class AttackResultPredictionInstance extends PredictionInstance {

	public static final String CLASS_BALL_LOSS = "CLASS_BALL_LOSS";
	public static final String CLASS_SHOT_ON_GOAL = "CLASS_SHOT_ON_GOAL";
	public static final String CLASS_BALL_OUT_OF_BOUNDS = "CLASS_BALL_OUT_OF_BOUNDS";

	private static final String ATTRIBUTE_TEAMMATE_IN_AREA = "ATTRIBUTE_TEAMMATE_IN_AREA";
	private static final String ATTRIBUTE_OPPPOSITE_IN_AREA = "ATTRIBUTE_OPPPOSITE_IN_AREA";
	private static final String ATTRIBUTE_PLAYER_PASS_SUCCESS = "ATTRIBUTE_PLAYER_PASS_SUCCESS";
	private static final String ATTRIBUTE_PLAYER_PASS_MISSED = "ATTRIBUTE_PLAYER_PASS_MISSED";
	private static final String ATTRIBUTE_PLAYER_BALLCONTACT = "ATTRIBUTE_PLAYER_BALLCONTACT";
	private static final String ATTRIBUTE_LAST_PLAYER_ID = "ATTRIBUTE_LAST_PLAYER_ID";
	private static final String ATTRIBUTE_CURRENT_PLAYER_ID = "ATTRIBUTE_CURRENT_PLAYER_ID";
	private static final String ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER = "ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER";
	private static final String ATTRIBUTE_CURRENT_PLAYER_X = "ATTRIBUTE_CURRENT_PLAYER_X";
	private static final String ATTRIBUTE_CURRENT_PLAYER_Y = "ATTRIBUTE_CURRENT_PLAYER_Y";
	private static final String ATTRIBUTE_CURRENT_PLAYER_DISTANCE = "ATTRIBUTE_CURRENT_PLAYER_DISTANCE";
	private static final String ATTRIBUTE_AREA = "ATTRIBUTE_AREA";
	private static final String ATTRIBUTE_PASS_COUNT = "ATTRIBUTE_PASS_COUNT";
	private static final String ATTRIBUTE_AVERAGE_VELOCITY = "ATTRIBUTE_AVERAGE_VELOCITY";

	public static final String ATTRIBUTE_CLASS = "class";

	private static final List<String> ATTRIBUTE_LIST = Arrays
			.asList(new String[] { ATTRIBUTE_TEAMMATE_IN_AREA,
					ATTRIBUTE_OPPPOSITE_IN_AREA, ATTRIBUTE_PLAYER_PASS_SUCCESS,
					ATTRIBUTE_PLAYER_PASS_MISSED, ATTRIBUTE_PLAYER_BALLCONTACT,
					ATTRIBUTE_LAST_PLAYER_ID, ATTRIBUTE_CURRENT_PLAYER_ID,
					ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER,
					ATTRIBUTE_CURRENT_PLAYER_X, ATTRIBUTE_CURRENT_PLAYER_Y,
					ATTRIBUTE_CURRENT_PLAYER_DISTANCE, ATTRIBUTE_AREA,
					ATTRIBUTE_PASS_COUNT, ATTRIBUTE_AVERAGE_VELOCITY,
					ATTRIBUTE_CLASS });

	private InstancesHeader instanceHeader;
	private Instance currentInstance;
	ArrayList<String> players;

	/**
	 * Instantiates the prediction instance and initializes it.
	 */
	public AttackResultPredictionInstance() {
		super();
	}

	@Override
	public void init() {

		/*
		 * attributes
		 */
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

		ArrayList<Attribute> attributes = new ArrayList<Attribute>();

		// number of team mates
		attributes.add(new Attribute(ATTRIBUTE_TEAMMATE_IN_AREA));

		attributes.add(new Attribute(ATTRIBUTE_OPPPOSITE_IN_AREA));

		// player pass success rate
		attributes.add(new Attribute(ATTRIBUTE_PLAYER_PASS_SUCCESS));

		attributes.add(new Attribute(ATTRIBUTE_PLAYER_PASS_MISSED));

		attributes.add(new Attribute(ATTRIBUTE_PLAYER_BALLCONTACT));

		attributes.add(new Attribute(ATTRIBUTE_LAST_PLAYER_ID, players));

		attributes.add(new Attribute(ATTRIBUTE_CURRENT_PLAYER_ID, players));

		// distance to nearest friendly player
		attributes.add(new Attribute(ATTRIBUTE_DISTANCE_TO_NEAREST_PLAYER));

		attributes.add(new Attribute(ATTRIBUTE_CURRENT_PLAYER_X));

		attributes.add(new Attribute(ATTRIBUTE_CURRENT_PLAYER_Y));

		attributes.add(new Attribute(ATTRIBUTE_CURRENT_PLAYER_DISTANCE));

		// field area
		ArrayList<String> areas = new ArrayList<String>();
		areas.add(Utils.FIELD_AREA_OWN_TEAM);
		areas.add(Utils.FIELD_AREA_MIDDLE);
		areas.add(Utils.FIELD_AREA_OPPONENTS);
		attributes.add(new Attribute(ATTRIBUTE_AREA, areas));

		attributes.add(new Attribute(ATTRIBUTE_PASS_COUNT));

		attributes.add(new Attribute(ATTRIBUTE_AVERAGE_VELOCITY));

		/*
		 * classes
		 */

		// result of attack
		ArrayList<String> classLabels = new ArrayList<String>();
		classLabels.add(CLASS_BALL_LOSS);
		classLabels.add(CLASS_BALL_OUT_OF_BOUNDS);
		classLabels.add(CLASS_SHOT_ON_GOAL);
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
	 * @param passCounter
	 * @param averageVelocity
	 */
	public void setAttributes(int numberOfTeammatesInArea,
			int numberOfOpponentsInArea, int playerPassesSuccessful,
			int playerPassesMissed, int ballContact, String lastPlayerId,
			String curentPlayerId, int distanceNearestPlayer, int currentX,
			int currentY, int playerDistance, boolean playerOnOwnSide,
			int passCounter, int averageVelocity) {
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
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_CURRENT_PLAYER_Y), currentY);

		currentInstance.setValue(ATTRIBUTE_LIST.indexOf(ATTRIBUTE_AREA),
				Utils.getFieldArea(currentX, !playerOnOwnSide));

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_CURRENT_PLAYER_DISTANCE),
				playerDistance);

		currentInstance.setValue(ATTRIBUTE_LIST.indexOf(ATTRIBUTE_PASS_COUNT),
				passCounter);

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_AVERAGE_VELOCITY),
				averageVelocity);
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
