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
 * Encapsulates an instance for training and attack result prediction.
 * 
 */
public class AttackResultPredictionInstance extends PredictionInstance {

	/**
	 * Class attribute for ball loss.
	 */
	public static final String CLASS_BALL_LOSS = "CLASS_BALL_LOSS";
	/**
	 * Class attribute for shot on goal.
	 */
	public static final String CLASS_SHOT_ON_GOAL = "CLASS_SHOT_ON_GOAL";
	/**
	 * Class attribute for ball out of bounds.
	 */
	public static final String CLASS_BALL_OUT_OF_BOUNDS = "CLASS_BALL_OUT_OF_BOUNDS";

	/**
	 * Number of team mates in area of a player.
	 */
	private static final String ATTRIBUTE_TEAMMATE_IN_AREA = "ATTRIBUTE_TEAMMATE_IN_AREA";
	/**
	 * Number of opponents in area of a player.
	 */
	private static final String ATTRIBUTE_OPPPOSITE_IN_AREA = "ATTRIBUTE_OPPPOSITE_IN_AREA";
	/**
	 * Number of successful passes divided by number of failed passes of a
	 * player.
	 */
	private static final String ATTRIBUTE_PLAYER_PASS_RATE = "ATTRIBUTE_PLAYER_PASS_RATE";
	/**
	 * Number of ball contacts of a player.
	 */
	private static final String ATTRIBUTE_PLAYER_BALLCONTACT = "ATTRIBUTE_PLAYER_BALLCONTACT";
	/**
	 * ID of a player made a pass.
	 */
	private static final String ATTRIBUTE_LAST_PLAYER_ID = "ATTRIBUTE_LAST_PLAYER_ID";
	/**
	 * ID of a player accepted a pass.
	 */
	private static final String ATTRIBUTE_CURRENT_PLAYER_ID = "ATTRIBUTE_CURRENT_PLAYER_ID";
	/**
	 * Distance to nearest team mate in meters.
	 */
	private static final String ATTRIBUTE_DISTANCE_TO_NEAREST_TEAMMATE = "ATTRIBUTE_DISTANCE_TO_NEAREST_TEAMMATE";
	/**
	 * X position of a player.
	 */
	private static final String ATTRIBUTE_CURRENT_PLAYER_X = "ATTRIBUTE_CURRENT_PLAYER_X";
	/**
	 * Y position of a player.
	 */
	private static final String ATTRIBUTE_CURRENT_PLAYER_Y = "ATTRIBUTE_CURRENT_PLAYER_Y";
	/**
	 * Accumulated run distance of a player.
	 */
	private static final String ATTRIBUTE_CURRENT_PLAYER_DISTANCE = "ATTRIBUTE_CURRENT_PLAYER_DISTANCE";
	/**
	 * The area a player is in, can be own area, middle area or opponent's area.
	 */
	private static final String ATTRIBUTE_AREA = "ATTRIBUTE_AREA";
	/**
	 * Number of passes occurred during attack.
	 */
	private static final String ATTRIBUTE_PASS_COUNT = "ATTRIBUTE_PASS_COUNT";
	/**
	 * Average velocity of the ball in opponent direction.
	 */
	private static final String ATTRIBUTE_AVERAGE_VELOCITY = "ATTRIBUTE_AVERAGE_VELOCITY";
	/**
	 * Distance to nearest opponent in meters.
	 */
	private static final String ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT = "ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT";

	/**
	 * The class label.
	 */
	public static final String ATTRIBUTE_CLASS = "AttackResultPrediction";

	/**
	 * List of all attributes to retrieve attribute indexes easily.
	 */
	private static final List<String> ATTRIBUTE_LIST = Arrays
			.asList(new String[] { ATTRIBUTE_TEAMMATE_IN_AREA,
					ATTRIBUTE_OPPPOSITE_IN_AREA, ATTRIBUTE_PLAYER_PASS_RATE,
					ATTRIBUTE_PLAYER_BALLCONTACT, ATTRIBUTE_LAST_PLAYER_ID,
					ATTRIBUTE_CURRENT_PLAYER_ID,
					ATTRIBUTE_DISTANCE_TO_NEAREST_TEAMMATE,
					ATTRIBUTE_CURRENT_PLAYER_X, ATTRIBUTE_CURRENT_PLAYER_Y,
					ATTRIBUTE_CURRENT_PLAYER_DISTANCE, ATTRIBUTE_AREA,
					ATTRIBUTE_PASS_COUNT, ATTRIBUTE_AVERAGE_VELOCITY,
					ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT, ATTRIBUTE_CLASS });

	/**
	 * Instance header with structure information.
	 */
	private InstancesHeader instanceHeader;
	/**
	 * The instance used for prediction and training.
	 */
	private Instance currentInstance;
	/**
	 * List of all players. Has to be created dynamically for use with other
	 * games.
	 */
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
		attributes.add(new Attribute(ATTRIBUTE_PLAYER_PASS_RATE));

		attributes.add(new Attribute(ATTRIBUTE_PLAYER_BALLCONTACT));

		attributes.add(new Attribute(ATTRIBUTE_LAST_PLAYER_ID, players));

		attributes.add(new Attribute(ATTRIBUTE_CURRENT_PLAYER_ID, players));

		// distance to nearest friendly player
		attributes.add(new Attribute(ATTRIBUTE_DISTANCE_TO_NEAREST_TEAMMATE));

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

		attributes.add(new Attribute(ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT));
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

	/**
	 * Resets the instance.
	 */
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
	 *            number of team mates in area of player who got the ball
	 * @param numberOfOpponentsInArea
	 *            number of opponents in area of player who got the ball
	 * @param playerPassesSuccessful
	 *            number of successful passes of player who made the pass
	 * @param playerPassesMissed
	 *            number of missed passes of player who made the pass
	 * @param ballContact
	 *            number of ball contacts of player who made the pass
	 * @param lastPlayerId
	 *            ID of player who made the pass
	 * @param curentPlayerId
	 *            ID of player who got the ball
	 * @param distanceNearestPlayer
	 *            distance to nearest team mate of player who got the ball
	 * @param currentX
	 *            x position of player who got the ball
	 * @param currentY
	 *            y of player who got the ball
	 * @param playerDistance
	 *            accumulated run distance of player who made the pass
	 * @param playerOnOwnSide
	 *            if player who got the ball is on his own side of the field
	 * @param passCounter
	 *            number of passes of player who got the ball
	 * @param averageVelocity
	 *            average velocity of the ball during the last seconds
	 */
	public void setAttributes(int numberOfTeammatesInArea,
			int numberOfOpponentsInArea, int playerPassesSuccessful,
			int playerPassesMissed, int ballContact, String lastPlayerId,
			String curentPlayerId, int distanceNearestPlayer, int currentX,
			int currentY, int playerDistance, boolean playerOnOwnSide,
			int passCounter, int distanceOpponent, int averageVelocity) {
		createEmptyInstance();

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_TEAMMATE_IN_AREA),
				numberOfTeammatesInArea);
		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_OPPPOSITE_IN_AREA),
				numberOfOpponentsInArea);

		int playerPassSuccessRate = (int) (((float) playerPassesSuccessful / ((float) playerPassesSuccessful + (float) playerPassesMissed)) * 100);
		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_PLAYER_PASS_RATE),
				playerPassSuccessRate);

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
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_DISTANCE_TO_NEAREST_TEAMMATE),
				distanceNearestPlayer);

		currentInstance.setValue(
				ATTRIBUTE_LIST.indexOf(ATTRIBUTE_DISTANCE_TO_NEAREST_OPPONENT),
				distanceOpponent);

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
