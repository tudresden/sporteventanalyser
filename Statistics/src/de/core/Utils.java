package de.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.espertech.esper.client.soda.InstanceOfExpression;

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
	
	public static void nearestPlayers(EventDecoder ed, Ball ball) {
//		System.out.println(ed.getEntityList().size());
		Map<Integer, Player> playerDistancesToBall = new TreeMap<Integer, Player>();
		for (Map.Entry<Integer, Entity> e:  ed.getEntityList().entrySet()) {
			if (e.getValue() instanceof Player) {
				Integer distance = (int) ball.distanceBetween(e.getValue().positionX, e.getValue().positionY, e.getValue().positionZ);
				playerDistancesToBall.put(distance, (Player) e.getValue());
			}
		}
//		System.out.println(playerDistancesToBall.keySet().);
		System.out.println("--------------");
//		for (int key: playerDistancesToBall.keySet())
//			System.out.println(playerDistancesToBall.get(key).getId());
		boolean first = true;
		for (Map.Entry<Integer, Player> p:  playerDistancesToBall.entrySet()) {
			if (first)
				System.out.println(p.getValue().id);
			first = false;
		}
	}
}
