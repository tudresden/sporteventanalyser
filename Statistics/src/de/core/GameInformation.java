package de.core;

public class GameInformation
{
	//Ball
	public int getActiveBallId()
	{
		return Main.main.currentActiveBallId;
	}

	//Player
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
	
	//Team
	private int[] a = {47, 49, 19, 53, 23, 57, 59}; //vorerst ohne Torwart
	private int[] b = {63, 65, 67, 69, 71, 73, 75}; //vorerst ohne Torwart
	
	//type a for team A, b for team B
	public int getTeamContacts(int[] teamkuerzel)
	{
		int contacts = 0;
		for(int i=0; i<teamkuerzel.length; i++)
		{
			contacts+=getPlayerBallContacts(teamkuerzel[i]);
		}
		return contacts;
	}
}

