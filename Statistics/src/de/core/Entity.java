package de.core;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public abstract class Entity
{
	public int positionX;
	public int positionY;
	public int positionZ;

	public int velocityX;
	public int velocityY;
	public int velocityZ;

	public int accelerationX;
	public int accelerationY;
	public int accelerationZ;

	public int acceleration;
	public int velocity;

	public int topSpeedX;
	public int topSpeedY;
	public int topSpeedZ;

	public long timeStamp = 0;
	protected float totalDistance = -1f;
	public int id;

	public Entity(int id, long timeStamp, int posX, int posY, int posZ, int velX, int velY, int velZ, int accX, int accY, int accZ, int acc, int vel)
	{
		this.id = id;
		this.timeStamp = timeStamp;

		this.positionX = posX;
		this.positionY = posY;
		this.positionZ = posZ;

		this.velocityX = velX;
		this.velocityY = velY;
		this.velocityZ = velZ;

		this.velocity = vel;
		this.acceleration = acc;

		this.accelerationX = accX;
		this.accelerationY = accY;
		this.accelerationZ = accZ;
	}

	public int getId()
	{
		return id;
	}

	public String getSymbol()
	{
		return Integer.toString(getId());
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public long getTimeStamp()
	{
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public float getTotalDistance()
	{
		return totalDistance;
	}

	public void setAcceleration(int acceleration)
	{
		this.acceleration = acceleration;
	}

	public int getAcceleration()
	{
		return acceleration;
	}

	public void setVelocity(int velocity)
	{
		this.velocity = velocity;
	}

	public int getVelocity()
	{
		return velocity;
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

	public void setTotalDistance(long totalDistance)
	{
		this.totalDistance = totalDistance;
	}

	public abstract void update(Event event);

	public void update2(Entity entity)
	{
		if (this.totalDistance < 0)
		{
			this.totalDistance = 0;
		}
		else
		{
			this.totalDistance += distanceBetween(entity.positionX, entity.positionY, entity.positionZ);
		}

		// update values
		this.positionX = entity.positionX;
		this.positionY = entity.positionY;
		this.positionZ = entity.positionZ;

		this.velocityX = entity.velocityX;
		this.velocityY = entity.velocityY;
		this.velocityZ = entity.velocityZ;

		this.accelerationX = entity.accelerationX;
		this.accelerationY = entity.accelerationY;
		this.accelerationZ = entity.accelerationZ;

		this.acceleration = entity.acceleration;
		this.velocity = entity.velocity;

		this.timeStamp = entity.getTimeStamp();
	}

	private float distanceBetweenSquared(int posX, int posY, int posZ)
	{
		double dX = positionX - posX;
		double dY = positionY - posY;
		double dZ = positionZ - posZ;
		return (float) (dX * dX + dY * dY + dZ * dZ);
	}

	private float distanceBetweenSquared(int posX, int posY)
	{
		double dX = positionX - posX;
		double dY = positionY - posY;
		return (float) (dX * dX + dY * dY);
	}

	public float distanceBetween(int posX, int posY, int posZ)
	{
		return (float) Math.sqrt(distanceBetweenSquared(posX, posY, posZ));
	}

	public float distanceBetween(int posX, int posY)
	{
		return (float) Math.sqrt(distanceBetweenSquared(posX, posY));
	}

	@Override
	public String toString()
	{
		return "Entity " + this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
	}
}
