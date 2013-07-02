package de.core;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;

public class Utils
{
	private static final int BIG_ENOUGH_INT = 16 * 1024;
	private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
	private static final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5D;

	private static Pattern pattern = Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})");

	public static float angleChange(int posX, int posY, int oldX, int oldY)
	{
		float dotProduct = posX * oldX + posY * oldY;
		double lengthVec1 = Math.sqrt((posX * posX) + (posY * posY));
		double lengthVec2 = Math.sqrt((oldX * oldX) + (oldY * oldY));
		float angle = (float) Math.acos(dotProduct / (lengthVec1 * lengthVec2));

		return angle;
	}

	public static int convertTimeToOffset(long timestamp)
	{
		return (int) fastFloor((timestamp - Config.GAMESTARTTIMESTAMP) / Config.TIMEFACTOR);
	}

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

	public static final int fastFloor(double x)
	{
		return (int) (x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
	}

	public static final int fastRound(double x)
	{
		return (int) (x + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
	}

	/**
	 * Returns the nearest sensor to the <code>Ball</code> object.
	 * 
	 * @param sensors
	 *            array of sensors
	 * @param ball
	 *            <code>Ball</code> object
	 * @return The shortest distance in mm.
	 */
	public static float getNearestSensor(Event[] sensors, Ball ball)
	{
		return Math.min(ball.distanceBetween(sensors[0].getPositionX(), sensors[0].getPositionY()), ball.distanceBetween(sensors[1].getPositionX(), sensors[1].getPositionY()));
	}

	public static float getDistanceBetween(int posx, int posy, int posxx, int posyy)
	{
		double dX = posx - posxx;
		double dY = posy - posyy;
		return (float) Math.sqrt((float) (dX * dX + dY * dY));
	}

	public static float getDistanceBetweenTwoPlayer(Player player1, Player player2)
	{
		Event obj1 = player1.getSensors()[0];
		Event obj2 = player1.getSensors()[1];
		Event obj3 = player2.getSensors()[0];
		Event obj4 = player2.getSensors()[1];

		float a = Math.min(getDistanceBetween(obj1.getPositionX(), obj1.getPositionY(), obj3.getPositionX(), obj3.getPositionY()), getDistanceBetween(obj1.getPositionX(), obj1.getPositionY(), obj4.getPositionX(), obj4.getPositionY()));
		float b = Math.min(getDistanceBetween(obj2.getPositionX(), obj2.getPositionY(), obj3.getPositionX(), obj3.getPositionY()), getDistanceBetween(obj2.getPositionX(), obj2.getPositionY(), obj4.getPositionX(), obj4.getPositionY()));
		return Math.min(a, b);
	}

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

	/**
	 * Checks if a pass is successful or not or if there is no pass.
	 * 
	 * @param a
	 *            first player
	 * @param b
	 *            second player
	 * @return 0 no pass, 1 pass, 2 pass failed.
	 */
	public static int pass(Player a, Player b)
	{
		if (a != null && b != null)
		{
			if (a.getTeam().equals(b.getTeam()) && (a.getId() != b.getId()))
			{
				return 1;
			}

			if (!a.getTeam().equals(b.getTeam()))
			{
				return 2;
			}
		}
		return 0;
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
		return (Config.GAMEFIELDMINX <= x) && (x <= Config.GAMEFIELDMAXX) && (Config.GAMEFIELDMINY <= y) && (y <= Config.GAMEFIELDMAXY);
	}

	/**
	 * Calculates all values of a HeatMapInit object needed to create new HeatMapGrid objects.
	 * 
	 * @return HeatMapInit object with all its information.
	 */
	public static HeatMapInit calculateHeatMapInit()
	{
		HeatMapInit heatMapInit = new HeatMapInit();

		int fieldWidthInMM = Config.GAMEFIELDMAXY + Math.abs(Config.GAMEFIELDMINY);
		int fieldHeightInMM = Config.GAMEFIELDMAXX + Math.abs(Config.GAMEFIELDMINX);

		heatMapInit.widthInCells = Config.heatMapWidthInCells;
		heatMapInit.widthResolution = fieldWidthInMM / Config.heatMapWidthInCells;
		heatMapInit.heightInCells = fastRound((float) fieldHeightInMM / heatMapInit.widthResolution);
		heatMapInit.heightResolution = fieldHeightInMM / heatMapInit.heightInCells;

		if (Config.GAMEFIELDMINY < 0)
		{
			heatMapInit.yMinNegativeAbs -= Config.GAMEFIELDMINY;
		}
		if (Config.GAMEFIELDMINX < 0)
		{
			heatMapInit.xMinNegativeAbs -= Config.GAMEFIELDMINX;
		}

		return heatMapInit;
	}

	public static String timeToHumanReadable(final long milliseconds)
	{
		return milliseconds/60000+" min, "+(milliseconds/1000)%60+" sec";
	}
}
