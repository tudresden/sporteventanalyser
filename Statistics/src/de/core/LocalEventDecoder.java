package de.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;

public class LocalEventDecoder
{
	private ExecutorService executor;
	private Esper esper;

	public LocalEventDecoder(Esper esper)
	{
		this.esper = esper;
	}

	public void decodeFileAsynchronous(int lines, String filePath)
	{
		executor = Executors.newFixedThreadPool(2);
		Callable<Void> c = new CallableDecode(this, lines, filePath);
		executor.submit(c);
	}

	public void decodeFile(final int lines, final String filePath)
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
					if (event.getTimestamp() >= Config.GAMESTARTTIMESTAMP)
					{
						esper.sendEvent(event);
					}
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
			Logger.getLogger("GZipReader").log(Level.SEVERE, "Warning: {0} not vaild format!", new Object[] { data });
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

	class CallableDecode implements Callable<Void>
	{
		private LocalEventDecoder eventDecoder;
		private int lines;
		private String filePath;

		public CallableDecode(LocalEventDecoder eventDecoder, int lines, String filePath)
		{
			this.eventDecoder = eventDecoder;
			this.lines = lines;
			this.filePath = filePath;
		}

		@Override
		public Void call() throws Exception
		{
			eventDecoder.decodeFile(lines, filePath);
			return null;
		}
	}
}