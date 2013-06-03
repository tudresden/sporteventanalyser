package de.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.espertech.esper.client.EPRuntime;

public class EventDecoder
{
	private ConcurrentHashMap<Integer, Entity> entityList;
	private EPRuntime cepRT;

	public EventDecoder(EPRuntime cepRT)
	{
		entityList = new ConcurrentHashMap<Integer, Entity>();
		createEntityList();
		this.cepRT = cepRT;
	}

	public ConcurrentHashMap<Integer, Entity> getEntityList()
	{
		return entityList;
	}

	public void decode(int lines, String filePath)
	{
		try
		{
			GZipReader reader = new GZipReader(filePath);

			String[] data;

			for (int i = 0; i < lines; i++)
			{
				data = reader.readNextLine();

				if (data != null)
				{
					Event event = decodeData(data);
					cepRT.sendEvent(event);
				}
			}
		}
		catch (Exception e)
		{
			Logger.getLogger("GZipReader").log(Level.SEVERE, "Error: {0}", new Object[] { e.getMessage() });
		}
	}

	public Event decodeData(String[] data)
	{
		final int length = data.length;

		if (data == null || length < 13)
		{
			Logger.getLogger("GZipReader").log(Level.WARNING, "Warning: {0} not vaild format!", new Object[] { data });
		}

		int id = Integer.parseInt(data[0]);
		long timeStamp = Long.parseLong(data[1]);

		return new Event(id, timeStamp, Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]), Integer.parseInt(data[9]), Integer.parseInt(data[10]), Integer.parseInt(data[11]), Integer.parseInt(data[12]));
	}

	public static void printData(String[] data)
	{
		System.out.println("==============================");
		System.out.println("Sender->" + data[0]);
		System.out.println("Zeitstempel->" + data[1]);
		System.out.println("Position (x)->" + data[2]);
		System.out.println("Position (y)->" + data[3]);
		System.out.println("Position (z)->" + data[4]);
		System.out.println("Geschwindigkeit->" + data[5]);
		System.out.println("Beschleunigung->" + data[6]);
		System.out.println("Geschw. (x)->" + data[7]);
		System.out.println("Geschw. (y)->" + data[8]);
		System.out.println("Geschw. (z)->" + data[9]);
		System.out.println("Beschl. (x)->" + data[10]);
		System.out.println("Beschl. (y)->" + data[11]);
		System.out.println("Beschl. (z)->" + data[12]);
	}

	public void createEntityList()
	{
		// key = left Leg, Leg 0 = left, 1 = right
		// Player(int id, String team, String name, int age, PlayingPosition playingPosition, int preferedFoot, int leftFootID, int
		// rightFootID)

		// Team GELB
		entityList.put(13, new Goalkeeper(13, "GELB", "Nick Gertje", 29, 1, 13, 14, 97, 98));

		entityList.put(47, new Player(47, "GELB", "Dennis Dotterweich", 21, PlayingPosition.LB, 0, 47, 16));
		entityList.put(16, entityList.get(47));

		entityList.put(49, new Player(49, "GELB", "Niklas Waelzlein", 19, PlayingPosition.DF, 1, 49, 88));
		entityList.put(88, entityList.get(49));

		entityList.put(19, new Player(19, "GELB", "Wili Sommer", 23, PlayingPosition.RB, 1, 19, 52));
		entityList.put(52, entityList.get(19));

		entityList.put(53, new Player(53, "GELB", "Philipp Harlass", 24, PlayingPosition.LM, 0, 53, 54));
		entityList.put(54, entityList.get(53));

		entityList.put(23, new Player(23, "GELB", "Roman Hartleb", 22, PlayingPosition.CM, 1, 23, 24));
		entityList.put(24, entityList.get(23));

		entityList.put(57, new Player(57, "GELB", "Erik Engelhardt", 21, PlayingPosition.RM, 1, 57, 58));
		entityList.put(58, entityList.get(57));

		entityList.put(59, new Player(59, "GELB", "Sandro Schneider", 27, PlayingPosition.CF, 0, 59, 28));
		entityList.put(28, entityList.get(59));

		// Team ROT
		entityList.put(61, new Goalkeeper(61, "ROT", "Leon Krapf", 26, 0, 61, 62, 99, 100));
//
		entityList.put(63, new Player(63, "ROT", "Kevin Baer", 18, PlayingPosition.LWB, 0, 63, 64));
		entityList.put(64, entityList.get(63));
//
		entityList.put(65, new Player(65, "ROT", "Luca Ziegler", 29, PlayingPosition.LB, 1, 65, 66));
		entityList.put(66, entityList.get(65));
//
		entityList.put(67, new Player(67, "ROT", "Ben Müller", 26, PlayingPosition.DM, 1, 67, 68));
		entityList.put(68, entityList.get(67));
//
		entityList.put(69, new Player(69, "ROT", "Vale Reitstetter", 22, PlayingPosition.RWB, 1, 69, 38)); 
		entityList.put(38, entityList.get(69));
//
		entityList.put(71, new Player(71, "ROT", "Christopher Lee", 23, PlayingPosition.RB, 1, 71, 40));
		entityList.put(40, entityList.get(71));
//
		entityList.put(73, new Player(73, "ROT", "Leon Heinze", 27, PlayingPosition.RM, 0, 73, 74));
		entityList.put(74, entityList.get(73));
//
		entityList.put(75, new Player(75, "ROT", "Leo Langhans", 21, PlayingPosition.LM, 1, 75, 44));
		entityList.put(44, entityList.get(75));

		// Balls
		entityList.put(4, new Ball(4));
		entityList.put(8, new Ball(8));
		entityList.put(10, new Ball(10));
		entityList.put(12, new Ball(12));

	}
}