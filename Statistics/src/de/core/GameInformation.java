package de.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;

public class GameInformation implements UpdateListener
{
	private int ballPosX = 0;
	private int ballPosY = 0;
	private int currentActiveBallId = 0; // ball id
	private int currentBallAcc = 1; // ball for counter in utils
	// Team
	private int[] a = { 47, 49, 19, 53, 23, 57, 59 }; // vorerst ohne Torwart
	private int[] b = { 63, 65, 67, 69, 71, 73, 75 }; // vorerst ohne Torwart

	private long lastBallPossessionTimeStamp = 0;
	private long timeAllBall = 0; // Difference of timestamps for counter
	private long timeBall = 0; // timestamp of lastBallEvent - BESSER: letztes Ball Objekt halten - ging nur nicht bei mir o.O genauso unten

	private Config config;
	private Player currentPlayer = null;

	public GameInformation()
	{
		config = new Config();
	}

	/**
	 * function for getting the ball.
	 * 
	 * @return active Ball in game.
	 */
	public int getActiveBallId()
	{
		return currentActiveBallId;
	}

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

	private int getBallPosX()
	{
		return ballPosX;
	}

	private int getBallPosY()
	{
		return ballPosY;
	}

	// Ball

	private Entity getEntityFromId(int id)
	{
		final ConcurrentHashMap<Integer, Entity> entityList = config.getEntityList();

		if (entityList.containsKey(id))
		{
			return entityList.get(id);
		}

		return null;
	}

	// Player

	private Player getNearestPlayer(Ball ball)
	{
		float nearestPlayerDistance = Float.MAX_VALUE;
		float distance;
		Player nearestPlayer = null;
		Player player = null;

		// for (Map.Entry<Integer, Entity> entry : eventDecoder.getEntityList().entrySet())
		// if (entry.getValue() instanceof Player)
		// {
		// player = (Player) entry.getValue();

		for (int id : Config.PLAYERIDS)
		{
			Entity entry = getEntityFromId(id);

			if (entry instanceof Player)
			{
				player = (Player) entry;

				distance = Utils.getNearestSensor(player.getSensors(), ball);
				// distance = ball.distanceBetween(player.getPositionX(), player.getPositionY());

				if (distance < Config.BALLPOSSESSIONTHRESHOLD && distance < nearestPlayerDistance)
				{
					nearestPlayerDistance = distance;
					nearestPlayer = player;

					// System.out.println("Distanz zum Ball: " + nearestPlayerDistance / 10f + "cm");
				}
			}
		}
		return nearestPlayer;
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
		Entity entity = getEntityFromId(id);
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
	 * function for BallPossession of one player
	 * 
	 * @param id
	 *            int-Value - playerID
	 * @return BallPossessionTime in picoseconds
	 */
	public long getPlayerBallPossession(int id)
	{
		Entity entity = getEntityFromId(id);
		if (entity instanceof Player)
		{
			return ((Player) entity).getBallPossessionTime();
		}
		else
		{
			return -1;
		}
	}

	/**
	 * function for runDistance of one player.
	 * 
	 * @param id
	 *            int-Value - playerID
	 * @return RunDistance of a given playerID.
	 */
	public float getPlayerDistance(int id)
	{
		return getEntityFromId(id).getTotalDistance();
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
		Entity entity = getEntityFromId(id);
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
	 * Get sum of all successful passes for a given player id.
	 * 
	 * @param id
	 *            int-Value - playerID
	 * @return Number of successful Passes of a given playerID.
	 */
	public int getPlayerPassesSuccessful(int id)
	{
		Entity entity = getEntityFromId(id);
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
				possessionTime += getPlayerBallPossession(teamkuerzel[i]);
			}
			for (int s = 0; s < b.length; s++)
			{
				possessionTime2 += getPlayerBallPossession(b[s]);
			}
			return ((possessionTime * 100) / (possessionTime + possessionTime2));
		}
		else if (teamkuerzel == b)
		{
			for (int i = 0; i < teamkuerzel.length; i++)
			{
				possessionTime += getPlayerBallPossession(teamkuerzel[i]);
			}
			for (int s = 0; s < a.length; s++)
			{
				possessionTime2 += getPlayerBallPossession(a[s]);
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

	private void setBallPosX(int posX)
	{
		this.ballPosX = posX;
	}

	private void setBallPosY(int posY)
	{
		this.ballPosY = posY;
	}

	private void shotOnGoal(LocalEventDecoder ed, Ball ball)
	{
		final int oldX = getBallPosX();
		final int oldY = getBallPosY();
		final int newX = ball.getPositionX();
		final int newY = ball.getPositionY();
		final int vecX = newX - oldX;
		final int vecY = newY - oldY;

		// siehe Blatt
		double xZumTor1 = (33941 - oldY) / vecY;
		double xZumTor2 = (-33968 - oldY) / vecY;
		double xAmTor1 = oldX + (xZumTor1 * vecX);
		double xAmTor2 = oldX + (xZumTor2 * vecX);

		if (xAmTor1 > Config.GOALONEMINX && xAmTor1 < Config.GOALONEMAXX && ball.getAcceleration() >= 15000000 && oldX != 0)
		{
			System.out.println("TORSCHUSS AUF TOR1");
		}
		if (xAmTor2 > Config.GOALTWOMINX && xAmTor2 < Config.GOALTWOMAXX && ball.getAcceleration() >= 15000000 && oldX != 0)
		{
			System.out.println("TORSCHUSS AUF TOR2");
		}

		setBallPosX(newX);
		setBallPosY(newY);
	}

	public void update(EventBean[] newData, EventBean[] oldData)
	{
		Event event = ((Event) newData[0].getUnderlying());
		Entity entity = getEntityFromId(event.getSender());
		final int gameTime = Utils.convertTimeToOffset(event.getTimestamp());

		if (gameTime >= 0)
		{
			if (entity instanceof Player)
			{
				Player player = (Player) entity;
				player.update(event);
			}
			else if (entity instanceof Ball)
			{
				Ball ball = (Ball) entity;

				/*
				 * Return if ball is no within the game field.
				 */
				if (!Utils.positionWithinField(event.getPositionX(), event.getPositionY()))
				{
					if (currentActiveBallId != 0 && currentActiveBallId == ball.getId())
					{
						System.out.println("============================================");
						System.out.println("Ball ID " + ball.getId() + " im außerhalb des Spielfeldes!");
						System.out.println("============================================");
						currentActiveBallId = 0;
					}

					return;
				}
				else
				{
					if (currentActiveBallId != ball.getId())
					{
						currentActiveBallId = ball.getId();
						System.out.println("============================================");
						System.out.println("Aktuelle Ball ID im Spiel: " + currentActiveBallId);
						System.out.println("============================================");
					}
				}
				ball.update(event);

				Player nearestPlayer = getNearestPlayer(ball);
				Player lastPlayer = currentPlayer;

				// if(lastPlayer!=null && nearestPlayer!=null)
				// {
				// Main.main.timeAllPlayer += nearestPlayer.timeStamp - lastPlayer.timeStamp;
				// if (Main.main.timeAllPlayer >= Math.pow(10, 11))
				// {
				// Main.main.currentBallPossessionId = 0;
				// Main.main.timeAllPlayer = 0;
				// }
				// }
				// Utils.shotOnGoal(Main.main.getEventDecoder(), ball);

				if (nearestPlayer != null)
				{
					// Function for BallContacts - only one ball contact all 50ms (see Utils.getBallHit)
					if (/* Main.main.currentBallPossessionId != nearestPlayer.id && */getBallHit(nearestPlayer, ball))
					{
						// Main.main.currentBallPossessionId = nearestPlayer.id;
						System.out.println("--------------");
						// print game time
						String time = String.format("%d min, %d sec", TimeUnit.SECONDS.toMinutes(gameTime), TimeUnit.SECONDS.toSeconds(gameTime) % 60);
						System.out.println("Spielzeit: " + time);
						System.out.println("Team: " + nearestPlayer.getTeam());
						System.out.println("Name des Spielers am Ball: " + nearestPlayer.getName());
						System.out.println("Laufstrecke: " + nearestPlayer.getTotalDistance() / 1000);

						nearestPlayer.setBallContacts(nearestPlayer.getBallContacts() + 1);
						System.out.println("Ballkontakte: " + nearestPlayer.getBallContacts());

						if (lastBallPossessionTimeStamp != 0 && lastPlayer != null)// && lastBall != null)
						{
							// Function for BallPossessionTime
							lastPlayer.setBallPossessionTime((lastPlayer.getBallPossessionTime()) + ((nearestPlayer.getTimeStamp() - lastBallPossessionTimeStamp)));
							System.out.println(lastPlayer.getName() + " " + lastPlayer.getBallPossessionTime());
							System.out.println(nearestPlayer.getName() + " " + nearestPlayer.getBallPossessionTime());
						}

						// Function for Passes
						if (lastPlayer != null)
						{
							if (Utils.pass(lastPlayer, nearestPlayer) == 1)
							{
								lastPlayer.setSuccessfulPasses(lastPlayer.getSuccessfulPasses() + 1);
								System.out.println("Spielzeit: " + time);
								System.out.println(lastPlayer.getName() + " - Erfolgreiche Pässe: " + lastPlayer.getSuccessfulPasses());
							}
							else if (Utils.pass(lastPlayer, nearestPlayer) == 2)
							{
								lastPlayer.setMissedPasses(lastPlayer.getMissedPasses() + 1);
								System.out.println("Spielzeit: " + time);
								System.out.println(lastPlayer.getName() + " - Fehlgeschlagene Pässe: " + lastPlayer.getMissedPasses());
							}
							else
								System.out.println("Kein Pass!");
						}

						// System.out.println(ball.timeStamp);
						// System.out.println(nearestPlayer.timeStamp);
						// if(lastPlayer != null) {System.out.println(Main.main.lastBallPossessionTimeStamp);}
						currentPlayer = nearestPlayer;
						lastBallPossessionTimeStamp = nearestPlayer.getTimeStamp();
						// Main.main.currentBall = ball;
					}
				}
				// Main.main.timePlayer = nearestPlayer.timeStamp;
			}
			else if (entity instanceof Goalkeeper)
			{
				Goalkeeper goalkeeper = (Goalkeeper) entity;
				goalkeeper.update(event);
				System.out.println(goalkeeper);
			}
		}
	}
}