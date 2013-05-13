package de.core;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Ball extends Entity
{
	public Ball(int id, long timeStamp, int posX, int posY, int posZ, int velX, int velY, int velZ, int accX, int accY, int accZ)
	{
		super(id, timeStamp, posX, posY, posZ, velX, velY, velZ, accX, accY, accZ);
	}

	public String toString()
	{
		return "Ball " + this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
	}
}
