package de.core;

import java.util.HashMap;
import java.util.Map;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Player extends Entity
{
	private int ballContacts;
	private long ballPossessionTime;
	private int goals;
	private HeatMapGrid heatmap;
	private Map<Integer, Event> Ids;
	private Pass lastPass;
	private boolean left = false;
	private int leftFootID;
	private int missedPasses;
	private String name;
	private int oldPositionX;
	private int oldPositionY;
	private PlayingPosition playingPosition;
	private boolean right = false;
	private int rightFootID;
	private int shots;
	private int shotsOnGoal;
	private int successfulPasses;
	private Team team;

	private Player(int id, long timeStamp, int posX, int posY, int posZ, int velX, int velY, int velZ, int accX, int accY, int accZ, int acc, int vel, Team team)
	{
		super(id, timeStamp, posX, posY, posZ, velX, velY, velZ, accX, accY, accZ, acc, vel);
		this.team = team;
		playingPosition = PlayingPosition.DF;
		Ids = new HashMap<Integer, Event>();
		this.heatmap = new HeatMapGrid(Config.heatMapInit);
	}

	public Player(int id, Team team, String name, PlayingPosition playingPosition, int leftFootID, int rightFootID)
	{
		this(id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, team);
		this.name = name;
		this.playingPosition = playingPosition;
		this.leftFootID = leftFootID;
		this.rightFootID = rightFootID;
		this.goals = 0;
		this.shotsOnGoal = 0;
		this.shots = 0;
		this.missedPasses = 0;
		this.successfulPasses = 0;
		this.ballContacts = 0;
		this.ballPossessionTime = 0;
		this.oldPositionX = 0;
		this.oldPositionY = 0;

		Ids.put(leftFootID, new Event(leftFootID, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		Ids.put(rightFootID, new Event(rightFootID, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
	}

	public int getBallContacts()
	{
		return ballContacts;
	}

	public long getBallPossessionTime()
	{
		return ballPossessionTime;
	}

	public int getGoals()
	{
		return goals;
	}

	public HeatMapGrid getHeatmap()
	{
		return heatmap;
	}

	/**
	 * Get the last pass of this player.
	 * 
	 * @return True if successful, false if not or null if there was no pass in the past.
	 */
	public Pass getLastPass()
	{
		return lastPass;
	}

	public int getMissedPasses()
	{
		return missedPasses;
	}

	public String getName()
	{
		return name;
	}

	public int getOldPositionX()
	{
		return oldPositionX;
	}

	public int getOldPositionY()
	{
		return oldPositionY;
	}

	public PlayingPosition getPlayingPosition()
	{
		return playingPosition;
	}

	public Event[] getSensors()
	{
		return new Event[] { Ids.get(leftFootID), Ids.get(rightFootID) };
	}

	public int getShots()
	{
		return shots;
	}

	public int getShotsOnGoal()
	{
		return shotsOnGoal;
	}

	public int getSuccessfulPasses()
	{
		return successfulPasses;
	}

	public Team getTeam()
	{
		return team;
	}

	public void setBallContacts(int ballContacts)
	{
		this.ballContacts = ballContacts;
	}

	public void setBallPossessionTime(long ballPossessionTime)
	{
		this.ballPossessionTime = ballPossessionTime;
	}

	public void setGoals(int goals)
	{
		this.goals = goals;
	}

	/**
	 * Set the last pass of this player.
	 * 
	 * @param pass
	 *            The new <code>Pass</code> object.
	 */
	public void setLastPass(Pass pass)
	{
		lastPass = pass;
	}

	public void setMissedPasses(int missedPasses)
	{
		this.missedPasses = missedPasses;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setOldPositionX(int oldPositionX)
	{
		this.oldPositionX = oldPositionX;
	}

	public void setOldPositionY(int oldPositionY)
	{
		this.oldPositionY = oldPositionY;
	}

	public void setPlayingPosition(PlayingPosition playingPosition)
	{
		this.playingPosition = playingPosition;
	}

	public void setShots(int shots)
	{
		this.shots = shots;
	}

	public void setShotsOnGoal(int shotsOnGoal)
	{
		this.shotsOnGoal = shotsOnGoal;
	}

	public void setSuccessfulPasses(int successfulPasses)
	{
		this.successfulPasses = successfulPasses;
	}

	public void setTeam(Team team)
	{
		this.team = team;
	}

	public String toString()
	{
		return "Player " + this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
	}

	@Override
	public void update(Event event)
	{
		// update events holder
		if (Ids.containsKey(event.getSender()))
		{
			Ids.put(event.getSender(), event);

			if (event.getSender() == leftFootID)
			{
				left = true;
			}
			else if (event.getSender() == rightFootID)
			{
				right = true;
			}

			updatePosition();
			updateVelocity();
			updateAcceleration();

			heatmap.incrementCellValue(event.getPositionY(), event.getPositionX());

			this.timeStamp = event.getTimestamp();
		}
	}

	private void updateAcceleration()
	{
		this.accelerationX = StrictMath.max(Ids.get(leftFootID).getAccelerationX(), Ids.get(rightFootID).getAccelerationX());
		this.accelerationY = StrictMath.max(Ids.get(leftFootID).getAccelerationY(), Ids.get(rightFootID).getAccelerationY());
		this.accelerationZ = StrictMath.max(Ids.get(leftFootID).getAccelerationZ(), Ids.get(rightFootID).getAccelerationZ());
		this.acceleration = StrictMath.max(Ids.get(leftFootID).getAcceleration(), Ids.get(rightFootID).getAcceleration());
	}

	private void updatePosition()
	{
		int newPosX = 0, newPosY = 0, newPosZ = 0;

		if (!left && !right)
		{
			return;
		}

		if (left && right)
		{
			newPosX = (Ids.get(leftFootID).getPositionX() + Ids.get(rightFootID).getPositionX()) / 2;
			newPosY = (Ids.get(leftFootID).getPositionY() + Ids.get(rightFootID).getPositionY()) / 2;
			newPosZ = (Ids.get(leftFootID).getPositionZ() + Ids.get(rightFootID).getPositionZ()) / 2;
		}
		else if (left && !right)
		{
			newPosX = Ids.get(leftFootID).getPositionX();
			newPosY = Ids.get(leftFootID).getPositionY();
			newPosZ = Ids.get(leftFootID).getPositionZ();
		}
		else if (!left && right)
		{
			newPosX = Ids.get(rightFootID).getPositionX();
			newPosY = Ids.get(rightFootID).getPositionY();
			newPosZ = Ids.get(rightFootID).getPositionZ();
		}

		// update total distance
		updateTotalDistance(newPosX, newPosY, newPosZ);

		setOldPositionX(this.positionX);
		setOldPositionY(this.positionY);

		this.positionX = newPosX;
		this.positionY = newPosY;
		this.positionZ = newPosZ;
	}

	private void updateTotalDistance(int x, int y, int z)
	{
		if (this.totalDistance < 0)
		{
			this.totalDistance = 0;
		}
		else
		{
			this.totalDistance += distanceBetween(x, y);
		}
	}

	private void updateVelocity()
	{
		this.velocityX = StrictMath.max(Ids.get(leftFootID).getVelocityX(), Ids.get(rightFootID).getVelocityX());
		this.velocityY = StrictMath.max(Ids.get(leftFootID).getVelocityY(), Ids.get(rightFootID).getVelocityY());
		this.velocityZ = StrictMath.max(Ids.get(leftFootID).getVelocityZ(), Ids.get(rightFootID).getVelocityZ());
		this.velocity = StrictMath.max(Ids.get(leftFootID).getVelocity(), Ids.get(rightFootID).getVelocity());
	}
}
