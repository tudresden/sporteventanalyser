package de.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Goalkeeper extends Player
{
	private int goals;
	private int shotsOnOwnGoal;
	private int heldShots;
	private int goalAgainst;
	private int passes;
	private int successfulPasses;
	private int ballContacts;
	private long ballPossessionTime;
	private int leftFootID;
	private int rightFootID;
	private int leftArmID;
	private int rightArmID;
	private Map<Integer, Integer> passesFrom;
	private Map<Integer, Integer> passesTo;
	private float runDistance;
	private boolean onBall;

	public Goalkeeper(int id, long timeStamp, int posX, int posY, int posZ, int velX, int velY, int velZ, int accX, int accY, int accZ, int acc, int vel, Team team)
	{
		super(id, timeStamp, posX, posY, posZ, velX, velY, velZ, accX, accY, accZ, acc, vel, team);
		this.playingPosition = PlayingPosition.GK;
	}

	public Goalkeeper(int id, Team team, String name, int age, int preferedFoot, int leftFootID, int rightFootID, int leftArmID, int rightArmID)
	{
		this(id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, team);
		this.name = name;
		this.age = age;
		this.playingPosition = PlayingPosition.GK;
		this.preferedFoot = preferedFoot;
		this.leftFootID = leftFootID;
		this.rightFootID = rightFootID;
		this.leftArmID = leftArmID;
		this.rightArmID = rightArmID;
		this.goals = 0;
		this.shotsOnOwnGoal = 0;
		this.heldShots = 0;
		this.goalAgainst = 0;
		this.passes = 0;
		this.successfulPasses = 0;
		this.ballContacts = 0;
		this.ballPossessionTime = 0;
		this.passesFrom = new HashMap<Integer, Integer>();
		this.passesTo = new HashMap<Integer, Integer>();
		this.runDistance = 0;
	}

	public String toString()
	{
		return "Goalkeeper " + this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
	}
}
