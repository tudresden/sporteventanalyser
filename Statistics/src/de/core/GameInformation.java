package de.core;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import predictions.Prophet;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticsFacade;

public class GameInformation implements UpdateListener
{
	private Config config;
	/**
	 * ball id
	 */
	private Ball currentActiveBall = null;
	/**
	 * ball for counter
	 */
	private int currentBallAcc = 1;

	private long currentGameTime = 0;
	private Player currentPlayer = null;

	/**
	 * false for no interruption, true for interruption
	 */
	private boolean gameInterruption = false;

	/**
	 * The timestamp of the last game interruption begin.
	 */
	private long gameInterruptionBegin = 0;

	/**
	 * The timestamp of the last game interruption end.
	 */
	private long gameInterruptionEnd = 0;

	/**
	 * false for half 1, true for half 2
	 */
	private boolean halftime = false;

	/**
	 * The timestamp of the last ball that was lost to the other team.
	 */
	private long lastBallLossTimeStamp = 0;

	/**
	 * The timestamp of the last ball that was outside the game field.
	 */
	private long lastBallOutsideTimeStamp = 0;
	/**
	 * The timestamp of the last ball possession.
	 */
	private long lastBallPossessionTimeStamp = 0;
	/**
	 * The timestamp of the last pushed of statistics data.
	 */
	private long lastPushedStatistics = 0;
	/**
	 * The timestamp of the last shot on goal.
	 */
	private long lastShotOnGoalTimeStamp = 0;

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Prophet prophet;
	private StatisticsFacade statisticsFacade;

	/**
	 * Difference of timestamps for counter.
	 */
	private long timeAllBall = 0;

	/**
	 * timestamp of lastBallEvent - BESSER: letztes Ball Objekt halten - ging nur nicht bei mir o.O genauso unten
	 */
	private long timeBall = 0;

	public GameInformation(StatisticsFacade statisticsFacade)
	{
		this.statisticsFacade = statisticsFacade;
		this.prophet = new Prophet(this);
		config = new Config();
	}

	/**
	 * Returns the active Ball
	 * 
	 * @return the Ball.
	 */
	public Ball getActiveBall()
	{
		return currentActiveBall;
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
		// Counter for time - add Difference of timestamp - only all 50ms one
		// BallHit!
		if (currentBallAcc == 0)
		{
			timeAllBall += ball.getTimeStamp() - timeBall;
		}

		if (timeAllBall > 500000000000L)
		{
			currentBallAcc = 1;
			timeAllBall = 0;
			timeBall = 0;
		}

		// ball-Beschleunigung >= 80m/s²?
		if (!gameInterruption && currentBallAcc == 1 && ball.getAvgAcceleration() >= 80000000)
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
	public Team getCurrentBallPossessionTeam()
	{
		return getCurrentBallPossessionPlayer().getTeam();
	}

	/**
	 * Returns the current relative game time in milliseconds.
	 * 
	 * @return The game time.
	 */
	public long getCurrentGameTime()
	{
		return currentGameTime;
	}

	/**
	 * Returns the distance to the next opponent of the player at the ball.
	 * 
	 * @return The distance in millimeters or -1 if there is no player at the ball.
	 */
	public float getDistanceOfNearestOpponent()
	{
		return getDistanceOfNextNearestPlayer(true);
	}

	/**
	 * Returns the distance to the next teammate of the player at the ball.
	 * 
	 * @return The distance in millimeters or -1 if there is no player at the ball.
	 */
	public float getDistanceOfNearestTeammate()
	{
		return getDistanceOfNextNearestPlayer(false);
	}

	/**
	 * Returns the distance to the next player of the player at the ball.
	 * 
	 * @return The distance in millimeters or -1 if there is no player at the ball.
	 */
	private float getDistanceOfNextNearestPlayer(boolean oppositeTeam)
	{
		Player activePlayer = getCurrentBallPossessionPlayer();

		/* break if no player owns the ball */
		if (activePlayer == null)
		{
			return -1;
		}

		float nearestDistance = Float.MAX_VALUE;
		float distance = 0;

		Player player = null;

		for (int id : Config.PLAYERIDS)
		{
			player = (Player) getEntityFromId(id);

			if (player.equals(activePlayer) || (oppositeTeam && player.getTeam() == activePlayer.getTeam() || (!oppositeTeam && player.getTeam() != activePlayer.getTeam())))
			{
				continue;
			}

			distance = Utils.getDistanceBetweenTwoPlayer(player, activePlayer);

			if (distance < nearestDistance)
			{
				nearestDistance = distance;
			}
		}

		return nearestDistance;
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
	 * Get the game interruption begin timestamp.
	 * 
	 * @return The relative game time in milliseconds
	 */
	public long getInterruptionBegin()
	{
		return gameInterruptionBegin;
	}

	/**
	 * Get the game interruption end timestamp.
	 * 
	 * @return The relative game time in milliseconds
	 */
	public long getInterruptionEnd()
	{
		return gameInterruptionEnd;
	}

	/**
	 * Get the relative game time in milliseconds of the last ball that was lost to the other team.
	 * 
	 * @return The relative game time in milliseconds.
	 */
	public long getLastBallLossTimeStamp()
	{
		return lastBallLossTimeStamp;
	}

	/**
	 * Get the relative game time in milliseconds of the ball that is not within the game field.
	 * 
	 * @return The game time in milliseconds.
	 */
	public long getLastBallOutsideTimeStamp()
	{
		return lastBallOutsideTimeStamp;
	}

	public long getLastBallPossessionTimeStamp()
	{
		return lastBallPossessionTimeStamp;
	}

	public long getLastPushedStatistics()
	{
		return lastPushedStatistics;
	}

	public long getLastShotOnGoalTimeStamp()
	{
		return lastShotOnGoalTimeStamp;
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
			player = (Player) getEntityFromId(id);

			distance = Utils.getNearestSensor(player.getSensors(), ball);

			if (distance < Config.BALLPOSSESSIONTHRESHOLD && distance < nearestPlayerDistance)
			{
				nearestPlayerDistance = distance;
				nearestPlayer = player;
			}
		}

		return nearestPlayer;
	}

	/**
	 * Returns the number of oppenents in a area.
	 * 
	 * @param meter
	 *            Radius for area
	 * @return The number of oppenents in a area in m.
	 */
	public int getOpponentsInArea(int meters)
	{
		return getPlayersInArea(meters, true);
	}

	/**
	 * Returns the sum of ballContacts of one player.
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
	 * Returns the absolute ball possession time of one player for a given player id.
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
	 * Returns the absolute sum of successful passes of the player for a given playerID.
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
	 * Returns the running direction of a given player.
	 * 
	 * @param id
	 *            ID of player-object
	 * @return Array consists of two values x,y for running direction of a player or {-1,-1} if there no direction already.
	 */
	public int[] getPlayerRunningDirection(int id)
	{
		// TODO: verbessern

		int[] array = new int[] { -1, -1 };
		Entity entity = getEntityFromId(id);
		Player player;
		int newX;
		int newY;
		int oldX;
		int oldY;
		if (entity != null && entity instanceof Player)
		{
			player = (Player) entity;
			newX = player.getPositionX();
			newY = player.getPositionY();
			oldX = player.getOldPositionX();
			oldY = player.getOldPositionY();
			if (oldX != 0 && oldY != 0 && newX != 0 && newY != 0)
			{
				array[0] = newX - oldX;
				array[1] = newY - oldY;
			}
		}
		return array;
	}

	/**
	 * Returns the number of players in a area.
	 * 
	 * @param meters
	 *            Radius for area
	 * @return The number of player in meters.
	 */
	private int getPlayersInArea(int meters, boolean oppositeTeam)
	{
		Ball activeBall = getActiveBall();
		Player activePlayer = getCurrentBallPossessionPlayer();

		/* break if no player owns the ball */
		if (activeBall == null || activePlayer == null)
		{
			return 0;
		}

		int numberOfTeammates = 0;

		Player player = null;

		for (int id : Config.PLAYERIDS)
		{
			player = (Player) getEntityFromId(id);

			if (player.equals(activePlayer) || (oppositeTeam && player.getTeam() == activePlayer.getTeam() || (!oppositeTeam && player.getTeam() != activePlayer.getTeam())))
			{
				continue;
			}

			if (Utils.getNearestSensor(player.getSensors(), activeBall) <= meters * 1000)
			{
				numberOfTeammates++;
			}
		}

		return numberOfTeammates;
	}

	private Prophet getProphet()
	{
		return prophet;
	}

	private StatisticsFacade getStatisticsFacade()
	{
		return statisticsFacade;
	}

	/**
	 * Get the overall time of ball possessions for a given team.
	 * 
	 * @param team
	 *            <code>Team</code> enumeration
	 * @return The overall time in percentage
	 */
	public long getTeamBallPossessionPercentage(Team team)
	{
		long possessionTimeTeam = 0;
		long possessionTimeTotal = 0;

		Player player = null;

		for (int id : Config.PLAYERIDS)
		{
			player = (Player) getEntityFromId(id);

			if (player.getTeam() == team)
			{
				possessionTimeTeam += player.getBallPossessionTime();
			}
			else
			{
				possessionTimeTotal += player.getBallPossessionTime();
			}
		}

		// calculate correct total time
		possessionTimeTotal += possessionTimeTeam;

		// prevent division by zero
		if (possessionTimeTotal == 0)
		{
			return 0;
		}

		return (possessionTimeTeam * 100) / possessionTimeTotal;
	}

	/**
	 * Returns sum of all ball contacts for a given team.
	 * 
	 * @param team
	 *            <code>Team</code> enumeration
	 * @return Number of BallContacts.
	 */
	public int getTeamContacts(Team team)
	{
		int contacts = 0;

		Player player = null;

		for (int id : Config.PLAYERIDS)
		{
			player = (Player) getEntityFromId(id);

			if (player.getTeam() == team)
			{
				contacts += player.getBallContacts();
			}
		}

		return contacts;
	}

	/**
	 * Returns the number of teammates in a area.
	 * 
	 * @param meters
	 *            Radius for area
	 * @return The number of teammates in a area in m.
	 */
	public int getTeammatesInArea(int meters)
	{
		return getPlayersInArea(meters, false);
	}

	/**
	 * Returns the team pass quote for a given team.
	 * 
	 * @param team
	 *            <code>Team</code> enumeration
	 * @return The percentage of successful passes.
	 */
	public int getTeamPassQuote(Team team)
	{
		int successfulPasses = 0;
		int missedPasses = 0;

		Player player = null;

		for (int id : Config.PLAYERIDS)
		{
			player = (Player) getEntityFromId(id);

			if (player.getTeam() != team)
			{
				continue;
			}

			successfulPasses += player.getSuccessfulPasses();
			missedPasses += player.getMissedPasses();
		}

		// calculate correct total time
		int totalPasses = successfulPasses + missedPasses;

		// prevent division by zero
		if (totalPasses == 0)
		{
			return 0;
		}

		return (100 * successfulPasses) / totalPasses;
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

	public boolean isPlayerOnOwnSide(Player player)
	{
		if (halftime == false)
		{
			if (player.getPositionY() >= 0 && player.getTeam() == Team.ROT)
			{
				return true;
			}
			if (player.getPositionY() < 0 && player.getTeam() == Team.GELB)
			{
				return true;
			}
		}
		else
		{
			if (player.getPositionY() < 0 && player.getTeam() == Team.ROT)
			{
				return true;
			}
			if (player.getPositionY() >= 0 && player.getTeam() == Team.GELB)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the active Ball
	 * 
	 * @param the
	 *            ball object
	 * @return The Ball.
	 */
	public Ball setActiveBall(Ball ball)
	{
		return currentActiveBall = ball;
	}

	/**
	 * Set the relative game time in milliseconds.
	 * 
	 * @params currentGameTime The game time.
	 */
	private void setCurrentGameTime(long currentGameTime)
	{
		this.currentGameTime = currentGameTime;
	}

	/**
	 * Set the game interruption begin timestamp.
	 * 
	 * @param timestamp
	 *            timestamp in picoseconds
	 */
	public void setInterruptionBegin(long timestamp)
	{
		this.gameInterruptionBegin = Utils.convertTimeToOffset(timestamp);
		gameInterruption = true;
	}

	/**
	 * Set the game interruption end timestamp.
	 * 
	 * @param timestamp
	 *            timestamp in picoseconds
	 */
	public void setInterruptionEnd(long timestamp)
	{
		this.gameInterruptionEnd = Utils.convertTimeToOffset(timestamp);
		gameInterruption = false;
	}

	/**
	 * Set the relative game time in milliseconds of the last ball that was lost to the other team.
	 * 
	 * @params milliseconds The relative game time in milliseconds.
	 */
	public void setLastBallLossTimeStamp(long milliseconds)
	{
		this.lastBallLossTimeStamp = milliseconds;
	}

	/**
	 * Set the relative game time in milliseconds of the ball that is not within the game field.
	 * 
	 * @params milliseconds The relative game time in milliseconds.
	 */
	public void setLastBallOutsideTimeStamp(long milliseconds)
	{
		this.lastBallOutsideTimeStamp = milliseconds;
	}

	public void setLastBallPossessionTimeStamp(long lastBallPossessionTimeStamp)
	{
		this.lastBallPossessionTimeStamp = lastBallPossessionTimeStamp;
	}

	public void setLastPushedStatistics(long lastPushedStatistics)
	{
		this.lastPushedStatistics = lastPushedStatistics;
	}

	public void setLastShotOnGoalTimeStamp(long lastShotOnGoalTimeStamp)
	{
		this.lastShotOnGoalTimeStamp = lastShotOnGoalTimeStamp;
	}

	/**
	 * Set the new pass from one player to another.
	 * 
	 * @param from
	 *            pass form player
	 * @param to
	 *            pass to player
	 */
	private void setPasses(Player from, Player to, String time)
	{
		if (from == null || from == null)
		{
			return;
		}

		final String name = from.getName();

		// pass successful
		if (Utils.pass(from, to) == 1)
		{
			from.setSuccessfulPasses(from.getSuccessfulPasses() + 1);
			from.setLastPass(new Pass(from.getId(), to.getId(), true, from.getTimeStamp()));
			logger.log(Level.INFO, "Spielzeit: {0} - {1} - Erfolgreiche Pässe: {2}", new Object[] { time, name, from.getSuccessfulPasses() });
		}
		// pass not successful
		else if (Utils.pass(from, to) == 2)
		{
			from.setMissedPasses(from.getMissedPasses() + 1);
			from.setLastPass(new Pass(from.getId(), to.getId(), false, from.getTimeStamp()));
			logger.log(Level.INFO, "Spielzeit: {0} - {1} - Fehlgeschlagene Pässe: {2}", new Object[] { time, name, from.getMissedPasses() });
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
		// TODO: Jon: Schauen ob er wirklich aufs Tor geht

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
			Ball activeBall = getActiveBall();

			// Return if ball is not within the game field.
			if (Utils.positionWithinField(event.getPositionX(), event.getPositionY()))
			{
				if (activeBall == null || !activeBall.equals(ball))
				{
					setActiveBall(ball);
					logger.log(Level.INFO, "Spielzeit: {0} - Ball ID {1} ist aktiver Ball!", new Object[] { time, ball.getId() });
				}
			}
			else
			{
				if (activeBall != null && activeBall.equals(ball))
				{
					logger.log(Level.INFO, "Spielzeit: {0} - Ball ID {1} außerhalb des Spielfeldes!", new Object[] { time, ball.getId() });
					setActiveBall(null);
					setLastBallOutsideTimeStamp(getCurrentGameTime());
				}

				// ball not within game field
				return;
			}

			// shotOnGoal(ball, ball.getPositionX(), ball.getPositionY(),
			// event.getPositionX(), event.getPositionY());

			ball.update(event);

			/* send data update to the visualization project */
			if (getStatisticsFacade() != null)
			{
				getStatisticsFacade().setPositionOfBall(ball.getPositionX(), ball.getPositionY(), ball.getPositionZ(), ball.getVelocityX(), ball.getVelocityY());
			}

			Player nearestPlayer = getNearestPlayer(ball);
			Player lastPlayer = currentPlayer;

			if (nearestPlayer != null)
			{
				// Function for BallContacts - only one ball contact all 50ms
				// (see getBallHit)
				if (getBallHit(nearestPlayer, ball))
				{
					// update ball contacts
					nearestPlayer.setBallContacts(nearestPlayer.getBallContacts() + 1);

					/* send data update to the visualization project */
					if (getStatisticsFacade() != null)
					{
						getStatisticsFacade().setBallContacs(nearestPlayer.getId(), nearestPlayer.getBallContacts());
					}

					System.out.println("--------------");
					// print game time
					System.out.println("Spielzeit: " + time);
					System.out.println("Team: " + nearestPlayer.getTeam());
					System.out.println("Name des Spielers am Ball: " + nearestPlayer.getName());
					System.out.println("Laufstrecke: " + nearestPlayer.getTotalDistance() / 1000 + "m");
					System.out.println("Teammitglieder in 20m Umkreis: " + getTeammatesInArea(20));
					System.out.println("Gegenspieler in 20m Umkreis: " + getOpponentsInArea(20));
					System.out.println("Nächster Mitspieler " + getDistanceOfNearestTeammate() / 1000 + "m");
					System.out.println("Nächster Gegenspieler " + getDistanceOfNearestOpponent() / 1000 + "m");
					System.out.println("Team A Passquote: " + getTeamPassQuote(Team.GELB) + "%");
					System.out.println("Team B Passquote: " + getTeamPassQuote(Team.ROT) + "%");

					System.out.println("Player 49 - Richtungsvektor: " + Arrays.toString(getPlayerRunningDirection(49)));

					System.out.println("Ballkontakte: " + nearestPlayer.getBallContacts());
					// System.out.println(getTeamContacts(a));
					// System.out.println(getTeamContacts(b));

					if (lastBallPossessionTimeStamp != 0 && lastPlayer != null)
					{
						// Function for BallPossessionTime
						lastPlayer.setBallPossessionTime(lastPlayer.getBallPossessionTime() + (nearestPlayer.getTimeStamp() - lastBallPossessionTimeStamp));
					}

					/* Calculate Passes */
					setPasses(lastPlayer, nearestPlayer, time);

					/* Send Pass statistic if available */
					if (getStatisticsFacade() != null)
					{
						getStatisticsFacade().setPassesMade(lastPlayer.getId(), lastPlayer.getMissedPasses() + lastPlayer.getSuccessfulPasses());
					}

					/* Set timestamp of team-ball-loss */
					if (lastPlayer != null && nearestPlayer.getTeam() != lastPlayer.getTeam())
					{
						setLastBallLossTimeStamp(getCurrentGameTime());
					}

					currentPlayer = nearestPlayer;
					lastBallPossessionTimeStamp = nearestPlayer.getTimeStamp();
				}
			}
		}
		else if (entity instanceof Player)
		{
			Player player = (Player) entity;
			player.update(event);

			/* send data update to the visualization project */
			if (getStatisticsFacade() != null)
			{
				getStatisticsFacade().setPositionOfPlayer(player.getId(), player.getPositionX(), player.getPositionY(), player.getVelocityX(), player.getVelocityY());
			}
		}
		else if (entity instanceof Goalkeeper)
		{
			((Goalkeeper) entity).update(event);
		}

		/* push statistics data to the prediction project */
		if (getCurrentGameTime() > getLastPushedStatistics() + Config.DATAPUSHINTERVAL)
		{
			setLastPushedStatistics(getCurrentGameTime());
			getProphet().updatePredictors();
		}
	}
}