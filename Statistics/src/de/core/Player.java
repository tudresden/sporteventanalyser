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
	private int leftFootID;
	private int rightFootID;
	private Map<Integer, Integer> passesFrom;
	private Map<Integer, Integer> passesTo;
	private float runDistance;
	protected boolean onBall;

	private HeatMapGrid heatmap;

	public Player(int id, long timeStamp, int posX, int posY, int posZ, int velX, int velY, int velZ, int accX, int accY, int accZ, String team)
	{
		super(id, timeStamp, posX, posY, posZ, velX, velY, velZ, accX, accY, accZ);
		this.team = team;
		playingPosition = PlayingPosition.DF;
	}

	public Player(int id, String team, String name, int age, PlayingPosition playingPosition, int preferedFoot, int leftFootID, int rightFootID)
	{
		this(id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, team);
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
}
