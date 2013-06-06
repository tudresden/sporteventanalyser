package de.core;

public class GameInformation
{
	public int getActiveBallId()
	{
		return Main.main.currentActiveBallId;
	}

	public float getPlayerDistance(int id)
	{
		return Main.main.getEntityFromId(id).getTotalDistance();
	}

	public int getPlayerBallContacts(int id)
	{
		Entity entity = Main.main.getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getBallContacts();
		}
		else
		{
			return -1;
		}
	}
	
	public int getPlayerPassesSuccessful(int id)
	{
		Entity entity = Main.main.getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getSuccessfulPasses();
		}
		else
		{
			return -1;
		}
	}
	
	public int getPlayerPassesMissed(int id)
	{
		Entity entity = Main.main.getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getMissedPasses();
		}
		else
		{
			return -1;
		}
	}
}

