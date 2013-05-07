package de.core;
/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Ball extends Entity
{
	public Ball(int id, long timeStamp, Vector3f position, Vector3f velocity, Vector3f acceleration)
	{
		super(id, timeStamp, position, velocity, acceleration);
	}

	public Ball(int id, long timeStamp, float posX, float posY, float posZ, float velX, float velY, float velZ, float accX, float accY, float accZ)
	{
		this(id, timeStamp, new Vector3f(posX, posY, posZ), new Vector3f(velX, velY, velZ), new Vector3f(accX, accY, accZ));
	}

	public String toString()
	{
		return "Ball " + super.getPosition();
	}
}
