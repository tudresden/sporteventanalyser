package de.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;

public class GameInformation implements UpdateListener
{
	private int currentGameTime = 0;
	/**
	 * ball id
	 */
	private int currentActiveBallId = 0;

	/**
	 * ball for counter
	 */
	private int currentBallAcc = 1;
	/**
	 * Team A vorerst ohne Torwart
	 */
	private int[] a = { 47, 49, 19, 53, 23, 57, 59 };
	/**
	 * Team B vorerst ohne Torwart
	 */
	private int[] b = { 63, 65, 67, 69, 71, 73, 75 };

	private long lastBallPossessionTimeStamp = 0;
	/**
	 * Difference of timestamps for counter.
	 */
	private long timeAllBall = 0;

	/**
	 * timestamp of lastBallEvent - BESSER: letztes Ball Objekt halten - ging nur nicht bei mir o.O genauso unten
	 */
	private long timeBall = 0;

	private Config config;
	private Player currentPlayer = null;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public GameInformation()
	{
		config = new Config();
	}

	/**
	 * Returns the active Ball id
	 * 
	 * @return the Ball id.
	 */
	public int getActiveBallId()
	{
		return currentActiveBallId;
	}

	/**
	 * Calculates if the <code>Ball</code> was hit.
	 * 
	 * @param nearestPlayer
	 *            the nearest <code>Player</code> to the <code>Ball</code>
	 * @param ball
	 *            the <code>Ball</code> object
	 * @return True if the <code>Ball</code> was hit or false.
	 */
	private boolean getBallHit(Player nearestPlayer, Ball ball)
	{
		// Counter for time - add Difference of timestamp - only all 10ms one BallHit!
		if (currentBallAcc == 0)
		{
			timeAllBall += ball.getTimeStamp() - timeBall;
		}

		if (timeAllBall > ((Math.pow(10, 11)) * 5))
		{
			currentBallAcc = 1;
			timeAllBall = 0;
			timeBall = 0;
		}

		// int a = ball.positionX;
		// int b = ball.positionY;
		// float output = (float) Math.toDegrees(angleChange(a, b, Main.main.ballPosX, Main.main.ballPosY));
		//
		// Main.main.ballPosX = a;
		// Main.main.ballPosY = b;

		if (currentBallAcc == 1 && ball.getAvgAcceleration() >= 80000000)
		{
			currentBallAcc = 0;
			timeBall = ball.getTimeStamp(); // setLastBallTime
			return true;
		}

		timeBall = ball.getTimeStamp(); // setLastBallTime
		return false;
	}

	/**
	 * Returns the current player that owns the ball.
	 * 
	 * @return <code>Player</code> object.
	 */
	public Player getCurrentBallPossessionPlayer()
	{
		return currentPlayer;
	}

	/**
	 * Returns the current team that owns the ball.
	 * 
	 * @return Name of the team.
	 */
	public String getCurrentBallPossessionTeam()
	{
		return getCurrentBallPossessionPlayer().getTeam();
	}

	/**
	 * Returns the current relative game time in seconds.
	 * 
	 * @return The game time.
	 */
	public int getCurrentGameTime()
	{
		return currentGameTime;
	}

	/**
	 * Returns the <code>Entity</code> for a given id.
	 * 
	 * @param id
	 *            the <code>Entity</code> id
	 * 
	 * @return The <code>Entity</code> object if exists or null.
	 */
	private Entity getEntityFromId(int id)
	{
		final ConcurrentHashMap<Integer, Entity> entityList = config.getEntityList();

		if (entityList.containsKey(id))
		{
			return entityList.get(id);
		}

		return null;
	}

	/**
	 * Calculates the nearest player to the ball.
	 * 
	 * @param ball
	 *            the <code>Ball</code> object
	 * 
	 * @return The nearest <code>Player</code>.
	 */
	private Player getNearestPlayer(Ball ball)
	{
		float nearestPlayerDistance = Float.MAX_VALUE;
		float distance;
		Player nearestPlayer = null;
		Player player = null;

		for (int id : Config.PLAYERIDS)
		{
			Entity entry = getEntityFromId(id);

			if (entry instanceof Player)
			{
				player = (Player) entry;

				distance = Utils.getNearestSensor(player.getSensors(), ball);

				if (distance < Config.BALLPOSSESSIONTHRESHOLD && distance < nearestPlayerDistance)
				{
					nearestPlayerDistance = distance;
					nearestPlayer = player;
				}
			}
		}
		return nearestPlayer;
	}

	/**
	 * function for sum of ballContacts of one player.
	 * 
	 * @param id
	 *            player id
	 * @return Number of BallContacts of a given playerID.
	 */
	public int getPlayerBallContacts(int id)
	{
		Entity entity = getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getBallContacts();
		}
		return -1;
	}

	/**
	 * Get the absolute ball possession time the player for a given player id.
	 * 
	 * @param id
	 *            player id
	 * @return Absolute ball possession time in picoseconds or -1 if player id was not found.
	 */
	public long getPlayerBallPossessionTime(int id)
	{
		Entity entity = getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getBallPossessionTime();
		}
		return -1;
	}

	/**
	 * Returns the absolute run distance of the player for a given playerID.
	 * 
	 * @param id
	 *            player id
	 * @return Absolute run distance in mm or -1 if player id was not found.
	 */
	public float getPlayerDistance(int id)
	{
		Entity entity = getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getTotalDistance();
		}
		return -1;
	}

	/**
	 * Returns the timestamp of the last pass of a given player id.
	 * 
	 * @param id
	 *            the player id
	 * @return The timestamp of the last pass or -1 if the player id was not found or there is no last pass.
	 */
	public long getPlayerLastPassTimestamp(int id)
	{
		Entity entity = getEntityFromId(id);
		if (entity instanceof Player)
		{
			Pass pass = ((Player) entity).getLastPass();

			if (pass != null)
			{
				return pass.getTimestamp();
			}

		}
		return -1;
	}

	/**
	 * Returns the absolute sum of missed passes of the player for a given playerID.
	 * 
	 * @param id
	 *            player id
	 * @return Number of missed Passes of a given playerID or -1 if player id was not found.
	 */
	public int getPlayerPassesMissed(int id)
	{
		Entity entity = getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getMissedPasses();
		}
		return -1;
	}

	/**
	 * Get sum of all successful passes for a given player id.
	 * 
	 * @param id
	 *            player id
	 * @return Number of successful Passes of a given playerID or -1 if player id was not found.
	 */
	public int getPlayerPassesSuccessful(int id)
	{
		Entity entity = getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getSuccessfulPasses();
		}
		return -1;
	}

	/**
	 * Get BallPossession of a given Team
	 * 
	 * @param teamkuerzel
	 *            int-Array of a given Team (a oder b)
	 * @return BallPossession Percentage
	 */
	public long getTeamBallPossession(int[] teamkuerzel)
	{
		long possessionTime = 0;
		long possessionTime2 = 0;

		if (teamkuerzel == a)
		{
			for (int i = 0; i < teamkuerzel.length; i++)
			{
				possessionTime += getPlayerBallPossessionTime(teamkuerzel[i]);
			}
			for (int s = 0; s < b.length; s++)
			{
				possessionTime2 += getPlayerBallPossessionTime(b[s]);
			}
			return ((possessionTime * 100) / (possessionTime + possessionTime2));
		}
		else if (teamkuerzel == b)
		{
			for (int i = 0; i < teamkuerzel.length; i++)
			{
				possessionTime += getPlayerBallPossessionTime(teamkuerzel[i]);
			}
			for (int s = 0; s < a.length; s++)
			{
				possessionTime2 += getPlayerBallPossessionTime(a[s]);
			}
			return ((possessionTime * 100) / (possessionTime + possessionTime2));
		}
		return -1;
	}

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
		for (int i = 0; i < teamkuerzel.length; i++)
		{
			contacts += getPlayerBallContacts(teamkuerzel[i]);
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
		for (int i = 0; i < teamkuerzel.length; i++)
		{
			successfulPasses += getPlayerPassesSuccessful(teamkuerzel[i]);
			missedPasses += getPlayerPassesMissed(teamkuerzel[i]);
		}
		return 100 * (successfulPasses / missedPasses);
	}

	/**
	 * Returns the result of the last pass of a given player id.
	 * 
	 * @param id
	 *            player id
	 * @return True if the last pass was successful or false.
	 */
	public boolean isPlayerLastPassSuccessful(int id)
	{
		Entity entity = getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getLastPass().isSuccessful();
		}
		return false;
	}

	/**
	 * Set the relative game time in seconds.
	 * 
	 * @params currentGameTime The game time.
	 */
	private void setCurrentGameTime(int currentGameTime)
	{
		this.currentGameTime = currentGameTime;
	}

	/**
	 * Set the new pass from one player to another.
	 * 
	 * @param from
	 *            pass form player
	 * @param to
	 *            pass to player
	 */
	private void setPasses(Player from, Player to)
	{
		if (from == null || from == null)
		{
			return;
		}

		final String name = from.getName();
		final String time = Utils.timeToHumanReadable(getCurrentGameTime());

		// pass successful
		if (Utils.pass(from, to) == 1)
		{
			from.setSuccessfulPasses(from.getSuccessfulPasses() + 1);
			from.setLastPass(new Pass(from.getId(), to.getId(), true, from.getTimeStamp()));
			logger.log(Level.INFO, "Spielzeit: {0} - {1} - Erfolgreiche P‰sse: {2}", new Object[] { time, name, from.getSuccessfulPasses() });
		}
		// pass not successful
		else if (Utils.pass(from, to) == 2)
		{
			from.setMissedPasses(from.getMissedPasses() + 1);
			from.setLastPass(new Pass(from.getId(), to.getId(), false, from.getTimeStamp()));
			logger.log(Level.INFO, "Spielzeit: {0} - {1} - Fehlgeschlagene P‰sse: {2}", new Object[] { time, name, from.getMissedPasses() });
		}
		// no pass
		else
		{
			logger.log(Level.INFO, "Spielzeit: {0} - {1} - Kein Pass!", new Object[] { time, name });
		}
	}

	/**
	 * Calculates if the <code>Ball</code> moves towards the goals
	 * 
	 * @param ball
	 *            the <code>Ball</code> object
	 * @param oldPosX
	 *            old X <code>Ball</code> position
	 * @param oldPosY
	 *            old Y <code>Ball</code> position
	 * @param newPosX
	 *            new X <code>Ball</code> position
	 * @param newPosY
	 *            new Y <code>Ball</code> position
	 */
	public void shotOnGoal(Ball ball, final int oldPosX, final int oldPosY, final int newPosX, final int newPosY)
	{
		final int vecX = newPosX - oldPosX;
		final int vecY = newPosY - oldPosY;

		// siehe Blatt
		double xZumTor1 = (33941 - oldPosY) / vecY;
		double xZumTor2 = (-33968 - oldPosY) / vecY;
		double xAmTor1 = oldPosX + (xZumTor1 * vecX);
		double xAmTor2 = oldPosX + (xZumTor2 * vecX);

		if (xAmTor1 > Config.GOALONEMINX && xAmTor1 < Config.GOALONEMAXX && ball.getAcceleration() >= 15000000 && oldPosX != 0)
		{
			System.out.println("TORSCHUSS AUF TOR1");
		}
		if (xAmTor2 > Config.GOALTWOMINX && xAmTor2 < Config.GOALTWOMAXX && ball.getAcceleration() >= 15000000 && oldPosX != 0)
		{
			System.out.println("TORSCHUSS AUF TOR2");
		}
	}

	public void update(EventBean[] newData, EventBean[] oldData)
	{
		Event event = ((Event) newData[0].getUnderlying());
		Entity entity = getEntityFromId(event.getSender());

		setCurrentGameTime(Utils.convertTimeToOffset(event.getTimestamp()));

		final String time = Utils.timeToHumanReadable(getCurrentGameTime());

		if (entity instanceof Ball)
		{
			Ball ball = (Ball) entity;

			// Return if ball is not within the game field.
			if (!Utils.positionWithinField(event.getPositionX(), event.getPositionY()))
			{
				if (currentActiveBallId != 0 && currentActiveBallId == ball.getId())
				{
					logger.log(Level.INFO, "Spielzeit: {0} - Ball ID {1} auﬂerhalb des Spielfeldes!", new Object[] { time, ball.getId() });
					currentActiveBallId = 0;
				}

				// ball not within game field
				return;
			}
			else
			{
				if (currentActiveBallId != ball.getId())
				{
					currentActiveBallId = ball.getId();
					logger.log(Level.INFO, "Spielzeit: {0} - Ball ID {1} ist aktiver Ball!", new Object[] { time, currentActiveBallId });
				}
			}

			// shotOnGoal(ball, ball.getPositionX(), ball.getPositionY(), event.getPositionX(), event.getPositionY());

			ball.update(event);

			Player nearestPlayer = getNearestPlayer(ball);
			Player lastPlayer = currentPlayer;

			if (nearestPlayer != null)
			{
				// Function for BallContacts - only one ball contact all 50ms (see Utils.getBallHit)
				if (getBallHit(nearestPlayer, ball))
				{
					System.out.println("--------------");
					// print game time
					System.out.println("Spielzeit: " + time);
					System.out.println("Team: " + nearestPlayer.getTeam());
					System.out.println("Name des Spielers am Ball: " + nearestPlayer.getName());
					System.out.println("Laufstrecke: " + nearestPlayer.getTotalDistance() / 1000 + "m");

					nearestPlayer.setBallContacts(nearestPlayer.getBallContacts() + 1);
					System.out.println("Ballkontakte: " + nearestPlayer.getBallContacts());

					if (lastBallPossessionTimeStamp != 0 && lastPlayer != null)
					{
						// Function for BallPossessionTime
						lastPlayer.setBallPossessionTime(lastPlayer.getBallPossessionTime() + (nearestPlayer.getTimeStamp() - lastBallPossessionTimeStamp));
						System.out.println(lastPlayer.getName() + " " + lastPlayer.getBallPossessionTime());
						System.out.println(nearestPlayer.getName() + " " + nearestPlayer.getBallPossessionTime());
					}

					// Function for Passes
					setPasses(lastPlayer, nearestPlayer);

					currentPlayer = nearestPlayer;
					lastBallPossessionTimeStamp = nearestPlayer.getTimeStamp();
				}
			}
		}
		else if (entity instanceof Player)
		{
			((Player) entity).update(event);
		}
		else if (entity instanceof Goalkeeper)
		{
			((Goalkeeper) entity).update(event);
		}
	}
}