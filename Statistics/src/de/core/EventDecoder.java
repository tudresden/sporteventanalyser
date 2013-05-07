package de.core;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.espertech.esper.client.EPRuntime;

public class EventDecoder
{
	private Map<Integer, Entity> cache;
	private EPRuntime cepRT;

	public EventDecoder(EPRuntime cepRT)
	{
		cache = new HashMap<Integer, Entity>();
		this.cepRT = cepRT;
	}

	public void decode(int lines, String filePath)
	{
		try
		{
			GZipReader reader = new GZipReader(filePath);

			String[] data;

			for (int i = 0; i < lines; i++)
			{
				// System.out.println(reader.readRawNextLine());

				data = reader.readNextLine();

				if (data != null)
				{
					// print data
					// printData(data);

					Entity entity = decodeData(data);

					cepRT.sendEvent(entity);

					if (!cache.containsKey(entity.getId()))
					{
						cache.put(entity.getId(), entity);
					}
					else
					{
						cache.get(entity.getId()).update(entity);
					}

					// float dist = cache.get(entity.getId()).getTotalDistance();
					//
					// if (dist != 0)
					// {
					// System.out.println(dist);
					// }

					// if (data[0].startsWith("98"))
					// {
					// reader.printStatistic(data);
					// }
				}
			}

			// for (Entry<Integer, Entity> e : cache.entrySet())
			// {
			// System.out.println(e.getValue());
			// }

			System.out.println(cache.size() + " verchieden IDs");
		}
		catch (Exception e)
		{
			Logger.getLogger("GZipReader").log(Level.SEVERE, "Error: {0}", new Object[] { e.getMessage() });
		}
	}

	public Entity decodeData(String[] data)
	{
		final int length = data.length;

		if (data == null || length < 13)
		{
			Logger.getLogger("GZipReader").log(Level.WARNING, "Warning: {0} not vaild format!", new Object[] { data });
		}

		int id = Integer.parseInt(data[0]);
		long timeStamp = Long.parseLong(data[1]);
		Vector3f position = new Vector3f(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]));
		Vector3f velocity = new Vector3f(Float.parseFloat(data[7]), Float.parseFloat(data[8]), Float.parseFloat(data[9]));
		Vector3f acceleration = new Vector3f(Float.parseFloat(data[10]), Float.parseFloat(data[11]), Float.parseFloat(data[12]));

		return new Entity(id, timeStamp, position, velocity, acceleration);
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
}
