package de.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Player extends Entity
{
	private String team;

	// neu
	protected String name;
	protected int age;
	protected PlayingPosition playingPosition;
	protected int preferedFoot;

	private int goals;
	private int shotsOnGoal;
	protected int shots;
	protected int passes;
	protected int successfulPasses;
	protected int ballContacts;
	protected long ballPossessionTime;
	public int leftFootID;
	public int rightFootID;
	private Map<Integer, Integer> passesFrom;
	private Map<Integer, Integer> passesTo;
	private float runDistance;
	protected boolean onBall;
	protected Event leftFootEvent;
	protected Event rightFootEvent;

	protected Map<Integer, Event> Ids;

	private HeatMapGrid heatmap;

	public Player(int id, long timeStamp, int posX, int posY, int posZ, int velX, int velY, int velZ, int accX, int accY, int accZ, int acc, int vel, String team)
	{
		super(id, timeStamp, posX, posY, posZ, velX, velY, velZ, accX, accY, accZ, acc, vel);
		this.team = team;
		playingPosition = PlayingPosition.DF;
		Ids = new HashMap<Integer, Event>();
	}

	public Player(int id, String team, String name, int age, PlayingPosition playingPosition, int preferedFoot, int leftFootID, int rightFootID)
	{
		this(id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, team);
		this.name = name;
		this.age = age;
		this.playingPosition = playingPosition;
		this.preferedFoot = preferedFoot;
		this.leftFootID = leftFootID;
		this.rightFootID = rightFootID;
		this.goals = 0;
		this.shotsOnGoal = 0;
		this.shots = 0;
		this.passes = 0;
		this.successfulPasses = 0;
		this.ballContacts = 0;
		this.ballPossessionTime = 0;
		this.passesFrom = new HashMap<Integer, Integer>();
		this.passesTo = new HashMap<Integer, Integer>();
		this.runDistance = 0;
		this.heatmap = new HeatMapGrid(20, 67925, 52477);

		Ids.put(leftFootID, new Event(leftFootID, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		Ids.put(rightFootID, new Event(rightFootID, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
	}

	public void updateHeatmap()
	{
		heatmap.incrementCellValue(super.positionY, super.positionX);
	}

	public int getId()
	{
		return id;
	}

	public String getTeam()
	{
		return team;
	}

	public void setTeam(String team)
	{
		this.team = team;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public HeatMapGrid getHeatmap()
	{
		return heatmap;
	}

	public String toString()
	{
		return "Player " + this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
	}

	private void updateTotalDistance(int x, int y, int z)
	{
		if (this.totalDistance < 0)
		{
			this.totalDistance = 0;
		}
		else
		{
			this.totalDistance += distanceBetween(x, y, z);
		}
	}

	private void updatePosition()
	{
		int newPosX = (Ids.get(leftFootID).getPositionX() + Ids.get(rightFootID).getPositionX()) / 2;
		int newPosY = (Ids.get(leftFootID).getPositionY() + Ids.get(rightFootID).getPositionY()) / 2;
		int newPosZ = (Ids.get(leftFootID).getPositionZ() + Ids.get(rightFootID).getPositionZ()) / 2;

		// update total distance
		updateTotalDistance(newPosX, newPosY, newPosZ);

		this.positionX = newPosX;
		this.positionY = newPosY;
		this.positionZ = newPosZ;
	}

	private void updateVelocity()
	{
		this.velocityX = (Ids.get(leftFootID).getVelocityX() + Ids.get(rightFootID).getVelocityX()) / 2;
		this.velocityY = (Ids.get(leftFootID).getVelocityY() + Ids.get(rightFootID).getVelocityY()) / 2;
		this.velocityZ = (Ids.get(leftFootID).getVelocityZ() + Ids.get(rightFootID).getVelocityZ()) / 2;
		this.velocity = (Ids.get(leftFootID).getVelocity() + Ids.get(rightFootID).getVelocity()) / 2;
	}

	private void updateAcceleration()
	{
		this.accelerationX = (Ids.get(leftFootID).getAccelerationX() + Ids.get(rightFootID).getAccelerationX()) / 2;
		this.accelerationY = (Ids.get(leftFootID).getAccelerationY() + Ids.get(rightFootID).getAccelerationY()) / 2;
		this.accelerationZ = (Ids.get(leftFootID).getAccelerationZ() + Ids.get(rightFootID).getAccelerationZ()) / 2;
		this.acceleration = (Ids.get(leftFootID).getAcceleration() + Ids.get(rightFootID).getAcceleration()) / 2;
	}

	@Override
	public void update(Event event)
	{
		// update events holder
		if (Ids.containsKey(event.getId()))
		{
			Ids.put(event.getId(), event);
		}

		updatePosition();
		updateVelocity();
		updateAcceleration();

		this.timeStamp = event.getTimeStamp();
	}

}
