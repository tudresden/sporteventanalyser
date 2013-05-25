package de.core;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Ball extends Entity
{
	public Ball(int id, long timeStamp, int posX, int posY, int posZ, int velX, int velY, int velZ, int accX, int accY, int accZ, int acc, int vel)
	{
		super(id, timeStamp, posX, posY, posZ, velX, velY, velZ, accX, accY, accZ, acc, vel);
	}

	public Ball(int id)
	{
		this(id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public String toString()
	{
		return "Ball " + this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
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

	@Override
	public void update(Event event)
	{
		updateTotalDistance(event.getPositionX(), event.getPositionY(), event.getPositionZ());

		// update values
		this.positionX = event.getPositionX();
		this.positionY = event.getPositionY();
		this.positionZ = event.getPositionZ();

		this.velocityX = event.getVelocityX();
		this.velocityY = event.getVelocityY();
		this.velocityZ = event.getVelocityZ();

		this.accelerationX = event.getAccelerationX();
		this.accelerationY = event.getAccelerationY();
		this.accelerationZ = event.getAccelerationZ();

		this.acceleration = event.getAcceleration();
		this.velocity = event.getVelocity();

		this.timeStamp = event.getTimeStamp();
	}
}
