package de.core;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static Player getNearestPlayer(EventDecoder eventDecoder, Ball ball)
	{
		float nearestPlayerDistance = Float.MAX_VALUE;
		float distance;
		Player nearestPlayer = null;
		Player player = null;

		for (Map.Entry<Integer, Entity> entry : eventDecoder.getEntityList().entrySet())
		{
			if (entry.getValue() instanceof Player)
			{
				player = (Player) entry.getValue();

				distance = ball.distanceBetween(player.positionX, player.positionY);

				if (distance < Main.main.ballPossessionThreshold && distance < nearestPlayerDistance)
				{
					nearestPlayerDistance = distance;
					nearestPlayer = player;
					player.ballContacts += 1;

					// System.out.println("Distanz zum Ball: " + nearestPlayerDistance / 10f + "cm");
				}
			}
		}

		return nearestPlayer;
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
		return (int) fastFloor((timeStamp - 10629342490369879L) / timeFormat);
	}
}
