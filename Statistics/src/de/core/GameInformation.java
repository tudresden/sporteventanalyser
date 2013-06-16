package de.core;

public class GameInformation
{
	//Ball
	
	/**
	 * function for getting the ball.
	 * @return active Ball in game.
	 */
	public int getActiveBallId()
	{
		return Main.main.currentActiveBallId;
	}
	

	//Player
	
	/**
	 * function for runDistance of one player.
	 * 
	 * @param id
	 *            int-Value - playerID
	 * @return RunDistance of a given playerID.
	 */
	public float getPlayerDistance(int id)
	{
		return Main.main.getEntityFromId(id).getTotalDistance();
	}

	/**
	 * function for sum of ballContacts of one player.
	 * 
	 * @param id
	 *            int-Value - playerID
	 * @return Number of BallContacts of a given playerID.
	 */
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
	
	/**
	 * function for sum of all successful passes of one player.
	 * 
	 * @param id
	 *            int-Value - playerID
	 * @return Number of successful Passes of a given playerID.
	 */
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
	
	/**
	 * function for sum of all missed passes of one player.
	 * 
	 * @param id
	 *            int-Value - playerID
	 * @return Number of missed Passes of a given playerID.
	 */
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
	
	/**
	 * function for BallPossession of one player
	 * 
	 * @param id
	 *            int-Value - playerID
	 * @return BallPossessionTime in picoseconds
	 */
	public long getPlayerBallPossession(int id)
	{
		Entity entity = Main.main.getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getBallPossessionTime();
		}
		else
		{
			return -1;
		}
	}
	
	//Team
	private int[] a = {47, 49, 19, 53, 23, 57, 59}; //vorerst ohne Torwart
	private int[] b = {63, 65, 67, 69, 71, 73, 75}; //vorerst ohne Torwart
	
	
	/**
	 * function for sum of all ballcontacts of one team.
	 * 
	 * @param teamkuerzel
	 *            int-Array of a given Team (a oder b)
	 * @return Number of BallContacts of one given Team.
	 */
	public int getTeamContacts(int[] teamkuerzel)
	{
		int contacts = 0;
		for(int i=0; i<teamkuerzel.length; i++)
		{
			contacts+=getPlayerBallContacts(teamkuerzel[i]);
		}
		return contacts;
	}
	
	
	/**
	 * function for teampassquote.
	 * 
	 * @param teamkuerzel
	 *            int-Array of a given Team (a oder b)
	 * @return percentage of successful passes.
	 */
	public int getTeamPassQuote(int[] teamkuerzel)
	{
		int successfulPasses = 0;
		int missedPasses = 0;
		for(int i=0; i<teamkuerzel.length; i++)
		{
			successfulPasses+=getPlayerPassesSuccessful(teamkuerzel[i]);
			missedPasses+=getPlayerPassesMissed(teamkuerzel[i]);
		}
		return 100*(successfulPasses/missedPasses);
	}
	
	/**
	 * function for BallPossession of a given Team
	 * 
	 * @param teamkuerzel
	 *            int-Array of a given Team (a oder b)
	 * @return BallPossession Percentage
	 */
	public long getTeamBallPossession(int[] teamkuerzel)
	{
		long possessionTime = 0;
		long possessionTime2 = 0;
		if(teamkuerzel == a)
		{
			for(int i=0; i<teamkuerzel.length; i++)
			{
				possessionTime+=getPlayerBallPossession(teamkuerzel[i]);
			}
			for(int s=0; s<b.length; s++)
			{
				possessionTime2+=getPlayerBallPossession(b[s]);
			}
			return ((possessionTime*100)/(possessionTime+possessionTime2));
		}
		else if(teamkuerzel == b)
		{
			for(int i=0; i<teamkuerzel.length; i++)
			{
				possessionTime+=getPlayerBallPossession(teamkuerzel[i]);
			}
			for(int s=0; s<a.length; s++)
			{
				possessionTime2+=getPlayerBallPossession(a[s]);
			}
			return ((possessionTime*100)/(possessionTime+possessionTime2));
		}
		return -1;
	}
	

}

