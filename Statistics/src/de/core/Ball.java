package de.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Ball extends Entity
{
	private boolean possibleContact = false;
	protected LinkedList<Event> Ids;
	private long avgAcceleration;

	public Ball(int id, long timeStamp, int posX, int posY, int posZ, int velX, int velY, int velZ, int accX, int accY, int accZ, int acc, int vel)
	{
		super(id, timeStamp, posX, posY, posZ, velX, velY, velZ, accX, accY, accZ, acc, vel);
		Ids = new LinkedList<Event>();
	}

	public Ball(int id)
	{
		this(id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Ids.add(new Event(id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
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
	
	private void updateList(Event jon){
		Ids.addFirst(jon);
		if(Ids.size()>5){
		Ids.removeLast();
		}
	}
	
	public long getAvgAcceleration(){
		avgAcceleration = calculateAvgAcceleration();
		return avgAcceleration;
	}
	
	public long calculateAvgAcceleration(){
		long sum = 0;
		for(Event joni :Ids){
			sum+=joni.getAcceleration();
		}
		return sum/Ids.size();
	}

	private void setAffected(boolean bool)
	{
		possibleContact = true;
	}

	public boolean isAffected()
	{
		return possibleContact;
	}
	/*
	public Event[] getSensors()
	{
		return new Event[] { Ids.get(id) };
	}*/

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

		// if (event.getAcceleration() > 55000000)
		// {
		// setAffected(true);
		// Main.main.shit = true;
		// }
		// else
		// {
		// setAffected(false);
		// Main.main.shit = false;
		// }

		this.acceleration = event.getAcceleration();
		this.velocity = event.getVelocity();

		this.timeStamp = event.getTimestamp();
		updateList(event);
	}
}
