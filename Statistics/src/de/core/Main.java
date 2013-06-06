package de.core;

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
public class Main
{
	private ExecutorService executor = Executors.newFixedThreadPool(4);
	private EventDecoder eventDecoder;
	private EsperTest esperTest = new EsperTest();
	public static Main main;
	public int ballPosX = 0;
	public int ballPosY = 0;
	public long timePlayer;
	public long timeAllPlayer = 0;
	public long timeBall;
	public long timeAllBall = 0;

	public Player currentPlayer = null;
	public int currentActiveBallId = 0; // ball id
	public int currentBallPossessionId = 0; // player
	public int currentBallAcc = 1; // ball
	public final static float BALLPOSSESSIONTHRESHOLD = 1000f; // 1000mm = 1m
	public final static long GAMESTARTTIMESTAMP = 10753295594424116L;
	public final static int BALLCONTACTTHRESHOLD = 0;

	public static final int GAMEFIELDMINX = -50;
	public static final int GAMEFIELDMAXX = 52489;
	public static final int GAMEFIELDMINY = -33960;
	public static final int GAMEFIELDMAXY = 33965;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		main = new Main();

		// main.test2();
		main.test();
	}

	public void test()
	{

		eventDecoder = new EventDecoder(esperTest.getCepRT());

		int[] playerIds = { 47, 16, 49, 88, 19, 52, 53, 54, 23, 24, 57, 58, 59, 28, 63, 64, 65, 66, 67, 68, 69, 38, 71, 40, 73, 74, 75, 44 };
		for (int player : playerIds)
		{
			esperTest.getAllFromSensorId(player, 100);
			// esperTest.getAllEventsFromSensorId(player); // every event
		}

		int[] ballIds = { 4, 8, 10 };
		for (int ball : ballIds)
		{
			esperTest.getAllFromSensorId(ball, 100);
			// esperTest.getAllEventsFromSensorId(ball); // every event
		}

		// start decoding the file async
		Callable<Void> c = new CallableDecode(eventDecoder, 100000000, "full-game.gz");
		executor.submit(c);

		System.out.println("============================================");
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

	public int getBallPosX()
	{
		return ballPosX;
	}

	public void setBallPosX(int posX)
	{
		this.ballPosX = posX;
	}

	public int getBallPosY()
	{
		return ballPosY;
	}

	public void setBallPosY(int posY)
	{
		this.ballPosY = posY;
	}

	class CallableDecode implements Callable<Void>
	{
		EventDecoder eventDecoder;
		int lines;
		String filePath;

		public CallableDecode(EventDecoder eventDecoder, int lines, String filePath)
		{
			this.eventDecoder = eventDecoder;
			this.lines = lines;
			this.filePath = filePath;
		}

		@Override
		public Void call() throws Exception
		{
			eventDecoder.decode(lines, filePath);
			return null;
		}
	}
}