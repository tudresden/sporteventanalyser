package de.core;
/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Player extends Entity
{
	private String team;

	public Player(int id, long timeStamp, Vector3f position, Vector3f velocity, Vector3f acceleration, String team)
	{
		super(id, timeStamp, position, velocity, acceleration);
		this.team = team;
	}

	public Player(int id, long timeStamp, float posX, float posY, float posZ, float velX, float velY, float velZ, float accX, float accY, float accZ, String team)
	{
		this(id, timeStamp, new Vector3f(posX, posY, posZ), new Vector3f(velX, velY, velZ), new Vector3f(accX, accY, accZ), team);
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
