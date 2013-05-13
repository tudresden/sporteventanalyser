package de.core;

public class Event implements Cloneable
{
	private int id;
	private long timeStamp;
	private int positionX;
	private int positionY;
	private int positionZ;
	private int velocity;
	private int acceleration;
	private int velocityX;
	private int velocityY;
	private int velocityZ;
	private int accelerationX;
	private int accelerationY;
	private int accelerationZ;

	public Event(int Sender, long timeStamp, int positionX, int positionY, int positionZ, int velocity, int acceleration, int velocityX, int velocityY, int velocityZ, int accelerationX, int accelerationY, int accelerationZ)
	{
		this.id = Sender;
		this.timeStamp = timeStamp;
		this.positionX = positionX;
		this.positionY = positionY;
		this.positionZ = positionZ;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.accelerationX = accelerationX;
		this.accelerationY = accelerationY;
		this.accelerationZ = accelerationZ;
	}

	public int getId()
	{
		return id;
	}

	public long getTimeStamp()
	{
		return timeStamp;
	}

	public int getPositionX()
	{
		return positionX;
	}

	public int getPositionY()
	{
		return positionY;
	}

	public int getPositionZ()
	{
		return positionZ;
	}

	public int getVelocity()
	{
		return velocity;
	}

	public int getAcceleration()
	{
		return acceleration;
	}

	public int getVelocityX()
	{
		return velocityX;
	}

	public int getVelocityY()
	{
		return velocityY;
	}

	public int getVelocityZ()
	{
		return velocityZ;
	}

	public int getAccelerationX()
	{
		return accelerationX;
	}

	public int getAccelerationY()
	{
		return accelerationY;
	}

	public int getAccelerationZ()
	{
		return accelerationZ;
	}

	public Event clone()
	{
		return new Event(id, timeStamp, positionX, positionY, positionZ, velocity, acceleration, velocityX, velocityY, velocityZ, accelerationX, accelerationY, accelerationZ);
	}

	@Override
	public String toString()
	{
		return this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
	}
}
