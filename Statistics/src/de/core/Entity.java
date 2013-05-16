package de.core;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Entity
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

	public int topSpeedX;
	public int topSpeedY;
	public int topSpeedZ;

	public long timeStamp = 0;
	private float totalDistance = -1f;
	public int id;

	public Entity(int id, long timeStamp, int posX, int posY, int posZ, int velX, int velY, int velZ, int accX, int accY, int accZ)
	{
		this.id = id;
		this.timeStamp = timeStamp;

		this.positionX = posX;
		this.positionY = posY;
		this.positionZ = posZ;

		this.velocityX = velX;
		this.velocityY = velY;
		this.velocityZ = velZ;

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

	public void setTotalDistance(long totalDistance)
	{
		this.totalDistance = totalDistance;
	}

	public void update(Entity entity)
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

		this.timeStamp = entity.getTimeStamp();
	}

	private float distanceBetweenSquared(int posX, int posY, int posZ)
	{
		double dX = positionX - posX;
		double dY = positionY - posY;
		double dZ = positionZ - posZ;
		return (float) (dX * dX + dY * dY + dZ * dZ);
	}

	public float distanceBetween(int posX, int posY, int posZ)
	{
		return (float) Math.sqrt(distanceBetweenSquared(posX, posY, posZ));
	}

	@Override
	public String toString()
	{
		return "Entity " + this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
	}
}
