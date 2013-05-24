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

				if (distance < Main.ballPossessionThreshold && distance < nearestPlayerDistance)
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

	public static boolean getBallHit(EventDecoder eventDecoder, Player nearestPlayer, Ball ball)
	{
		// System.out.println(nearestPlayer.getAcceleration() / 1000000f);

		if (nearestPlayer.getAcceleration() / 1000000f >= 15f)
		{
			// System.out.println(nearestPlayer.getAcceleration() / 1000000);
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
		return true;
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
		// return (int) fastFloor((timeStamp - 10629342490369879L) / timeFormat);
		return (int) fastFloor((timeStamp - 10753295594424116L) / timeFormat);
	}
	
	public static void shotOnGoal(EventDecoder ed, Ball ball)
	{
		int oldX = Main.main.getBallPosX();
		int oldY = Main.main.getBallPosY();
		int newX = ball.positionX;
		int newY = ball.positionY;
		int vecX = newX - oldX;
		int vecY = newY - oldY;
		
		//siehe Blatt
		double xZumTor1 = (33941/vecY)-oldY;
		double xZumTor2 = (-33968/vecY)-oldY;
		double xAmTor1 = oldX+(xZumTor1*vecX);
		double xAmTor2 = oldX+(xZumTor2*vecX);
				
		if((xAmTor1>22578.5)&&(xAmTor1<29898.5)&&((ball.getAcceleration()/1000000f)>=15f)&&(oldX!=0)){
			System.out.println("TORSCHUSS AUF TOR1");
		}
		if((xAmTor2>22560.0)&&(xAmTor2<29880.0)&&((ball.getAcceleration()/1000000f)>=15f)&&(oldX!=0)){
			System.out.println("TORSCHUSS AUF TOR2");
		}
		Main.main.setBallPosX(newX);
		Main.main.setBallPosY(newY);
	}
	/*
	 * public static void ballContacts(EventDecoder ed, Ball ball) { // System.out.println(ed.getEntityList().size()); Map<Integer, Integer>
	 * allBallContacts = new TreeMap<Integer, Integer>(); for (Map.Entry<Integer, Entity> e: ed.getEntityList().entrySet()){ if(e.getValue()
	 * instanceof Ball){ //velocity=Geschwindigkeit; acceleration=Beschleunigung //Unterschiede nehmen also p1-p2 = vektor^^ int a =
	 * e.getValue().positionX; int b = e.getValue().positionY; float output = angleBetween(a,b,Main.main.getPosX(),Main.main.getPosY());
	 * //Grad = Bogenmaß * 180/Pi int output1 = (int) ((output*180)/Math.PI); if (output1>20){ System.out.println("--------------");
	 * System.out.println("Aktuelle Zeit: "+Math.ceil((ball.timeStamp-10629342490369879L)/(Math.pow(10, 12))-4));
	 * System.out.println(ball.timeStamp); System.out.println(output1); System.out.println(e.getValue().positionX);
	 * System.out.println(e.getValue().positionY); System.out.println(Main.main.getPosX()); System.out.println(Main.main.getPosY()); }
	 * Main.main.setPosX(e.getValue().positionX); Main.main.setPosY(e.getValue().positionY); int diffX = Main.main.getPosX()-ball.positionX;
	 * System.out.println("Unterschied X: "+diffX); System.out.println("Gespeichert: "+Main.main.getPosX());
	 * Main.main.setPosX(e.getValue().positionX); long diffX = Main.main.getTimestamp()-ball.timeStamp;
	 * System.out.println("Unterschied Zeit: "+diffX); System.out.println("Gespeichert: "+Main.main.getTimestamp());
	 * Main.main.setTimestamp(ball.timeStamp); // System.out.println("Zeitstempel: "+ball.timeStamp); //
	 * System.out.println("AccerlerationX: "+e.getValue().accelerationX); //
	 * System.out.println("AccerlerationY: "+e.getValue().accelerationY); //
	 * System.out.println("AccerlerationZ: "+e.getValue().accelerationZ); // System.out.println("PositionX: "+e.getValue().positionX); //
	 * System.out.println("PositionY: "+e.getValue().positionY); // System.out.println("PositionZ: "+e.getValue().positionZ); //
	 * System.out.println("TopSpeedX: "+e.getValue().topSpeedX); // System.out.println("TopSpeedY: "+e.getValue().topSpeedY); //
	 * System.out.println("TopSpeedZ: "+e.getValue().topSpeedZ); // System.out.println("VelocityX: "+e.getValue().velocityX); //
	 * System.out.println("VelocityY: "+e.getValue().velocityY); // System.out.println("VelocityZ: "+e.getValue().velocityZ); } } } public
	 * static float angleBetween(int posX, int posY, int oldX, int oldY) { float dotProduct = posX*oldX+posY*oldY; double lengthVec1 =
	 * Math.sqrt((posX*posX)+(posY*posY)); double lengthVec2 = Math.sqrt((oldX*oldX)+(oldY*oldY)); float angle =
	 * (float)Math.acos(dotProduct/(lengthVec1*lengthVec2)); return angle; }
	 */
}
