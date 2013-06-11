package de.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;

public class Utils
{
	public static boolean isNumeric(String input)
	{
		try
		{
			Integer.parseInt(input);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	private static Pattern pattern = Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})");

	public static long dateParseRegExp(String period)
	{
		Matcher matcher = pattern.matcher(period);
		if (matcher.matches())
		{
			return Long.parseLong(matcher.group(1)) * 3600000L + Long.parseLong(matcher.group(2)) * 60000 + Long.parseLong(matcher.group(3)) * 1000 + Long.parseLong(matcher.group(4));
		}
		else
		{
			throw new IllegalArgumentException("Invalid format " + period);
		}
	}

	private static float getNearestSensor(Event[] sensors, Ball ball)
	{
		return Math.min(ball.distanceBetween(sensors[0].getPositionX(), sensors[0].getPositionY()), ball.distanceBetween(sensors[1].getPositionX(), sensors[1].getPositionY()));
	}

	public static Player getNearestPlayer(EventDecoder eventDecoder, Ball ball)
	{
		float nearestPlayerDistance = Float.MAX_VALUE;
		float distance;
		Player nearestPlayer = null;
		Player player = null;

		// for (Map.Entry<Integer, Entity> entry : eventDecoder.getEntityList().entrySet())
		// if (entry.getValue() instanceof Player)
		// {
		// player = (Player) entry.getValue();

		int[] playerIds = { 47, 49, 19, 53, 23, 57, 59, 63, 65, 67, 69, 71, 73, 75 };
		for (int id : playerIds)
		{
			Entity entry = eventDecoder.getEntityList().get(id);

			if (entry instanceof Player)
			{
				player = (Player) entry;

				distance = getNearestSensor(player.getSensors(), ball);
				// distance = ball.distanceBetween(player.getPositionX(), player.getPositionY());

				if (distance < Main.BALLPOSSESSIONTHRESHOLD && distance < nearestPlayerDistance)
				{
					nearestPlayerDistance = distance;
					nearestPlayer = player;

					// System.out.println("Distanz zum Ball: " + nearestPlayerDistance / 10f + "cm");
				}
			}
		}
		return nearestPlayer;
	}

	public static boolean getBallHit(EventDecoder eventDecoder, Player nearestPlayer, Ball ball)
	{
		// System.out.println(nearestPlayer.getAcceleration() / 1000000f);
		// long zeit = nearestPlayer.timeStamp-Main.main.timePlayer;
		// Main.main.timeAll += zeit;
		// if(Main.main.timeAll >= Math.pow(10, 12)){
		// Main.main.currentBallPossessionId = 0;
		// Main.main.timeAll = 0;

		if (Main.main.currentBallAcc == 0)
		{
			long zeit = ball.timeStamp - Main.main.timeBall;
			Main.main.timeAllBall += zeit;
		}

		if (Main.main.timeAllBall > ((Math.pow(10, 11)) * 5))
		{
			Main.main.currentBallAcc = 1;
			Main.main.timeAllBall = 0;
		}

		int a = ball.positionX;
		int b = ball.positionY;
		float output = (float) Math.toDegrees(angleChange(a, b, Main.main.ballPosX, Main.main.ballPosY));

		Main.main.ballPosX = a;
		Main.main.ballPosY = b;

		if (Main.main.currentBallAcc == 1 && ball.getAvgAcceleration() >= 80000000)// && ((nearestPlayer.getSensors()[0].getAcceleration()
																					// >=
																					// 24000000)||(nearestPlayer.getSensors()[1].getAcceleration()
																					// >= 24000000)))
		{
			// System.out.println(nearestPlayer.getAcceleration() / 1000000);
			Main.main.currentBallAcc = 0;
			return true;
		}

		// Player bla = getNearestPlayer(eventDecoder, ball);
		//
		// if (bla.getId() == nearestPlayer.getId())
		// {
		// if ((int) bla.getAcceleration() / 1000000 >= 15)
		// {
		// return true;
		// }
		// return false;
		// }
		Main.main.timeBall = ball.timeStamp;
		return false;
	}

	private static final int BIG_ENOUGH_INT = 16 * 1024;
	private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
	private static final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5D;

	public static final int fastRound(double x)
	{
		return (int) (x + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
	}

	public static final int fastFloor(double x)
	{
		return (int) (x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
	}

	private final static double timeFormat = Math.pow(10, 12);

	public static int convertTimeToOffset(long timeStamp)
	{
		return (int) fastFloor((timeStamp - Main.GAMESTARTTIMESTAMP) / timeFormat);
	}

	public static void shotOnGoal(EventDecoder ed, Ball ball)
	{
		int oldX = Main.main.getBallPosX();
		int oldY = Main.main.getBallPosY();
		int newX = ball.positionX;
		int newY = ball.positionY;
		int vecX = newX - oldX;
		int vecY = newY - oldY;

		// siehe Blatt
		double xZumTor1 = (33941 - oldY) / vecY;
		double xZumTor2 = (-33968 - oldY) / vecY;
		double xAmTor1 = oldX + (xZumTor1 * vecX);
		double xAmTor2 = oldX + (xZumTor2 * vecX);

		if ((xAmTor1 > 22578.5) && (xAmTor1 < 29898.5) && ((ball.getAcceleration() / 1000000f) >= 15f) && (oldX != 0))
		{
			System.out.println("TORSCHUSS AUF TOR1");
		}
		if ((xAmTor2 > 22560.0) && (xAmTor2 < 29880.0) && ((ball.getAcceleration() / 1000000f) >= 15f) && (oldX != 0))
		{
			System.out.println("TORSCHUSS AUF TOR2");
		}
		Main.main.setBallPosX(newX);
		Main.main.setBallPosY(newY);
	}

	/**
	 * Checks if the given position is within the game field.
	 * 
	 * @param x
	 *            position
	 * @param y
	 *            position
	 * @return True if position is within the game field or false if not.
	 */
	public static boolean positionWithinField(int x, int y)
	{
		return (Main.GAMEFIELDMINX <= x) && (x <= Main.GAMEFIELDMAXX) && (Main.GAMEFIELDMINY <= y) && (y <= Main.GAMEFIELDMAXY);
	}

	public static float angleChange(int posX, int posY, int oldX, int oldY)
	{
		float dotProduct = posX * oldX + posY * oldY;
		double lengthVec1 = Math.sqrt((posX * posX) + (posY * posY));
		double lengthVec2 = Math.sqrt((oldX * oldX) + (oldY * oldY));
		float angle = (float) Math.acos(dotProduct / (lengthVec1 * lengthVec2));

		return angle;
	}

	/**
	 * Checks if a pass is successful or not or if there is no pass
	 * 
	 * @param a
	 *            player
	 * @param b
	 *            player
	 * @return 0 no pass, 1 pass, 2 pass failed
	 */
	public static int pass(Player a, Player b)
	{
		if (a.getTeam().equals(b.getTeam()) && a.id != b.id)
		{
			return 1;
		}

		if (!a.getTeam().equals(b.getTeam()))
		{
			return 2;
		}
		return 0;
	}
}
