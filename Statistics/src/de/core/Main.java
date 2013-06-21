package de.core;


/**
 * Auslesen vonner Megadatei für ein Programm, welches es eigentlich schon vom Fraunhofer gibt
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
/**
 * @author Alrik Geselle, Richard John, Tommy Kubika
 * 
 */
public class Main
{
	private Esper esper;
	private GameInformation gameInformation;

	public Main()
	{
		this.esper = new Esper();
		gameInformation = new GameInformation();

		for (int player : Config.PLAYERSENSORIDS)
		{
			esper.getAllFromSensorId(player, 100, gameInformation);
			// esperTest.getAllEventsFromSensorId(player, gameInformation); // every event
		}

		for (int ball : Config.BALLIDS)
		{
			esper.getAllFromSensorId(ball, 100, gameInformation);
			// esperTest.getAllEventsFromSensorId(ball, gameInformation); // every event
		}
	}

	public void sendEvent(Object object)
	{
		esper.sendEvent(object);
	}

	public GameInformation getGameInformation()
	{
		return gameInformation;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Main main = new Main();

		/**
		 * Start decoding the local file async
		 */
		LocalEventDecoder eventDecoder = new LocalEventDecoder(main.esper);
		eventDecoder.decodeFileAsynchronous(100000000, "full-game.gz");
	}
}