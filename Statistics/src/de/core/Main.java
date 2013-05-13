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
public class Main
{
	private ExecutorService executor = Executors.newFixedThreadPool(4);
	private EventDecoder eventDecoder;
	public static Main main;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		main = new Main();
		// main.test();
		main.test2();
	}

	public void test2()
	{
		GZipReader reader;
		String[] data;
		String[] name;

		try
		{
			reader = new GZipReader("referee-events1.tar.gz");

			int lines = 1000000;

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date;

			for (int i = 0; i < lines; i++)
			{
				// System.out.println(reader.readRawNextLine());

				data = reader.readNextCsvLine();

				if (data == null)
				{
					break;
				}

				if (data.length == 5 && Utils.isNumeric(data[0]))
				{
					int eventNumber = Integer.parseInt(data[0]);

					if (eventNumber != 6014 && eventNumber != 6015)
					{
						continue;
					}

					System.out.print(eventNumber);
					// System.out.print(data[1]);

					name = data[1].replace("\"", "").split("\\s* \\s*");

					// Vorname
					System.out.print(name[0]);
					// Nachname
					System.out.print(name[1]);

					System.out.print(data[2]);

					try
					{
						date = dateFormat.parse("1970-01-01 " + data[2]);
						System.out.print(date.getTime());
					}
					catch (ParseException e)
					{
						// e.printStackTrace();
					}

					System.out.println(data[3]);

				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void test()
	{
		EsperTest esperTest = new EsperTest();

		eventDecoder = new EventDecoder(esperTest.getCepRT());

		// get sensor id 47 from the past 30sek
		// esperTest.getSensorId(47, 300);

		// start decoding the file
		// eventDecoder.decode(100, "full-game.gz");

		// start decoding the file async
		Callable<Void> c = new CallableDecode(eventDecoder, 100000000, "full-game.gz");
		executor.submit(c);

		System.out.println("============================================");

		// esperTest.getAllFromSensorId(47, 1); // the past 30 seconds
		esperTest.getAllFromSensorIdPerMillisecond(47, 10); // every 10ms
		// esperTest.getTimedFromSensorId(47, 10); //
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