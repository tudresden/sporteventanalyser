package de.core;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Entity
{
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f acceleration;
	private Vector3f topSpeed;
	private long timeStamp = 0;
	private float totalDistance = 0;
	private int id;
	String symbol = "47";

	public Entity(int id, long timeStamp, Vector3f position, Vector3f velocity, Vector3f acceleration)
	{
		this.id = id;
		this.symbol = Integer.toString(getId());
		this.timeStamp = timeStamp;
		this.setPosition(position);
		this.setVelocity(velocity);
		this.setAcceleration(acceleration);
		this.setTopSpeed(new Vector3f());
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

	public Vector3f getPosition()
	{
		return position;
	}

	public float getPositionX()
	{
		return position.x;
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public Vector3f getAcceleration()
	{
		return acceleration;
	}

	public void setAcceleration(Vector3f acceleration)
	{
		this.acceleration = acceleration;
	}

	public Vector3f getVelocity()
	{
		return velocity;
	}

	public void setVelocity(Vector3f velocity)
	{
		this.velocity = velocity;
	}

	public Vector3f getTopSpeed()
	{
		return topSpeed;
	}

	public void setTopSpeed(Vector3f topSpeed)
	{
		this.topSpeed = topSpeed;
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
		this.totalDistance += this.position.distanceBetween(entity.getPosition());

		// update values
		this.position = entity.getPosition();
		this.velocity = entity.getVelocity();
		this.acceleration = entity.getAcceleration();
		this.timeStamp = entity.getTimeStamp();
	}

	@Override
	public String toString()
	{
		return "Entity " + this.getId() + " " + this.getTimeStamp() + " " + this.getPosition() + " " + this.getVelocity() + " " + this.getAcceleration();
	}
}
