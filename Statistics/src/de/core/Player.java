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
	private String name;
	private int age;
	private PlayingPosition playingPosition;
	private int preferedFoot;

	private int goals;
	private int shotsOnGoal;
	private int shots;
	private int passes;
	private int successfulPasses;
	private int ballContacts;
	private long ballPossessionTime;
	private int leftFootID;
	private int rightFootID;
	private Map<Integer, Integer> passesFrom;
	private Map<Integer, Integer> passesTo;
	private float runDistance;
	private boolean onBall;

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
	}

	public String getTeam()
	{
		return team;
	}

	public void setTeam(String team)
	{
		this.team = team;
	}

	public String toString()
	{
		return "Player " + this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
	}
}
