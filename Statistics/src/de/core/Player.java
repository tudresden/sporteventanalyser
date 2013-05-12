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
	
	//neu
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
	

	public Player(int id, long timeStamp, Vector3f position, Vector3f velocity, Vector3f acceleration, String team)
	{
		super(id, timeStamp, position, velocity, acceleration);
		this.team = team;
		playingPosition = PlayingPosition.DF;
	}

	public Player(int id, long timeStamp, float posX, float posY, float posZ, float velX, float velY, float velZ, float accX, float accY, float accZ, String team)
	{
		this(id, timeStamp, new Vector3f(posX, posY, posZ), new Vector3f(velX, velY, velZ), new Vector3f(accX, accY, accZ), team);
	}
	
	public Player(int id, String team, String name, int age, PlayingPosition playingPosition, int preferedFoot, int leftFootID, int rightFootID)
	{
		super(id, 0, new Vector3f(), new Vector3f(), new Vector3f());
		this.team=team;
		this.name=name;
		this.age=age;
		this.playingPosition=playingPosition;
		this.preferedFoot=preferedFoot;
		this.leftFootID=leftFootID;
		this.rightFootID=rightFootID;	
		this.goals=0;
		this.shotsOnGoal=0;
		this.shots=0;
		this.passes=0;
		this.successfulPasses=0;
		this.ballContacts=0;
		this.ballPossessionTime=0;
		this.passesFrom = new HashMap<Integer, Integer>();
		this.passesTo = new HashMap<Integer, Integer>();
		this.runDistance=0;
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
		return "Player " + super.getPosition();
	}
}
