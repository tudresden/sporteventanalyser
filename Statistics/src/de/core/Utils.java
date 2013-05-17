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
				Integer distance = (int) ball.distanceBetween1(e.getValue().positionX, e.getValue().positionY);
				//if(distance<=2000){
				playerDistancesToBall.put(distance, (Player) e.getValue());
				//}
			}
		}
		
		 boolean first = true;
		  for (Map.Entry<Integer, Player> p:  playerDistancesToBall.entrySet()) {
		   if (first==true){
		    System.out.println("--------------");
		    System.out.println("Aktuelle Zeit: "+Math.ceil((ball.timeStamp-10629342490369879L)/(Math.pow(10, 12)))+" s");
		    System.out.println("Name des Spielers am Ball: "+p.getValue().getName());
		    System.out.println("Spieler: (ID: "+p.getValue().getId()+") --- Zeitstempel: "+p.getValue().timeStamp);
		    System.out.println("Ball:    (ID: 0"+ball.id+") --- Zeitstempel: "+ball.timeStamp);
		   first = false;}
		  }
	}
}
