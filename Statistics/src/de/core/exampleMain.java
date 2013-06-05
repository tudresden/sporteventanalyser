package de.core;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.esper.EsperTest;

/**
 * Auslesen vonner Megadatei für ein Programm, welches es eigentlich schon vom Fraunhofer gibt
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class exampleMain
{
	private ExecutorService executor = Executors.newFixedThreadPool(4);
	private EventDecoder eventDecoder;
	private EsperTest esperTest = new EsperTest();
	public static exampleMain main;
	public int ballPosX = 0;	//for Angle-Calc
	public int ballPosY = 0;
	public long timePlayer;
	public long timeAllPlayer = 0;
	public long timeBall;
	public long timeAllBall = 0;

	public int currentBallPossessionId = 0; //player
	public int currentBallAcc = 1;			//ball
	public final static float BALLPOSSESSIONTHRESHOLD = 1000f; // 1000mm = 1m
	public final static long GAMESTARTTIMESTAMP = 10753295594424116L;
	public final static int BALLCONTACTTHRESHOLD = 0;

	public boolean shit = false;
	public int out = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		main = new exampleMain();
		main.test();

	}

	public void test()
	{
		SomeThread thread = new SomeThread();
		thread.setDaemon(true);
		executor.execute(thread);
		
		
		eventDecoder = new EventDecoder(esperTest.getCepRT());

		System.out.println("============================================");

		// int[] playerIds = { 47, 49, 19, 53, 23, 57, 59, 63, 65, 67, 69, 71, 73, 75 };
		int[] playerIds = { 47, 16, 49, 88, 19, 52, 53, 54, 23, 24, 57, 58, 59, 28, 63, 64, 65, 66, 67, 68, 69, 38, 71, 40, 73, 74, 75, 44 };
		for (int player : playerIds)
		{
			esperTest.getAllFromSensorId(player, 100);
		}
		esperTest.getAllFromSensorId(4, 100);
	}

	public EventDecoder getEventDecoder()
	{
		return eventDecoder;
	}

	public Entity getEntityFromId(int id)
	{
		if (getEventDecoder().getEntityList().containsKey(id))
		{
			return getEventDecoder().getEntityList().get(id);
		}

		return null;
	}

	public class SomeThread extends Thread {

	    @Override
	    public void run() {
	        try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	}
}
