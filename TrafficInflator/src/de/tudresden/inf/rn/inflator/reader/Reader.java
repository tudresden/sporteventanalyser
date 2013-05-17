package de.tudresden.inf.rn.inflator.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.tudresden.inf.rn.mobilis.sea.client.proxy.Event;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.SportEventAnalyserProxy;

public class Reader {

	private static final int MAX_EVENTS_PER_MESSAGE = 50;
	private static final int MIN_EVENTS_PER_MESSAGE = 100;
	private static final boolean SEND_DATA_IN_PLAY_TIME = true;
	private static int eventCounter = 0, messageCounter = 0;
	private static long startPlayTime = 0, startSystemTime = 0;
	private static long timeOfCycleStart = System.currentTimeMillis();

	/**
	 * Queue for <code>Event</code>'s (also used as mutex)
	 */
	private LinkedList<Event> queue;

	/**
	 * The <code>RandomAccessFile</code> to read from
	 */
	private RandomAccessFile rndAccFile;

	private long bufferLength;

	private SportEventAnalyserProxy proxy;

	private FileInputStream f;

	public Reader(File file, long bufferLength, SportEventAnalyserProxy proxy)
			throws FileNotFoundException {
		f = new FileInputStream(file);
		// rndAccFile = new RandomAccessFile(file, "r");
		// BufferLength should be passed as ms, but data is passed in
		// picoseconds
		this.bufferLength = bufferLength * 1000 * 1000 * 1000;
		queue = new LinkedList<Event>();

		this.proxy = proxy;

		// Process actions in new Threads
		Thread reader = new Thread() {
			public void run() {
				// processFile();
				processTransformedData();
			}
		};
		reader.setPriority(Thread.MAX_PRIORITY);
		Thread processor = new Thread() {
			public void run() {
				processQueue();
			}
		};
		processor.setPriority(Thread.MAX_PRIORITY);
		reader.start();
		processor.start();
	}

	private Event processEntry(String[] eventData) {
		// Assign data directly
		return new Event(Integer.valueOf(eventData[0]),
				Long.valueOf(eventData[1]), Integer.valueOf(eventData[2]),
				Integer.valueOf(eventData[3]), Integer.valueOf(eventData[4]),
				Integer.valueOf(eventData[5]), Integer.valueOf(eventData[6]),
				Integer.valueOf(eventData[7]), Integer.valueOf(eventData[8]),
				Integer.valueOf(eventData[9]), Integer.valueOf(eventData[10]),
				Integer.valueOf(eventData[11]), Integer.valueOf(eventData[12]));
	}

	/**
	 * Tries to process data as fast as the given proxy allows it (However, this
	 * method does not work with plain text nor uses a FileChannel to fetch
	 * data)
	 */
	private void processTransformedData() {
		try {
			// rndAccFile.seek(0);
			// while (true) {
			// try {
			// proxy.EventNotification("mobilis@sea/SEA",
			// rndAccFile.readInt(), rndAccFile.readLong(),
			// rndAccFile.readInt(), rndAccFile.readInt(),
			// rndAccFile.readInt(), rndAccFile.readInt(),
			// rndAccFile.readInt(), rndAccFile.readInt(),
			// rndAccFile.readInt(), rndAccFile.readInt(),
			// rndAccFile.readInt(), rndAccFile.readInt(),
			// rndAccFile.readInt());
			// } catch (EOFException e) {
			// break;
			// }
			// }
			long lT;

			FileChannel ch = f.getChannel();
			ByteBuffer bb = ByteBuffer.allocateDirect(262136);
			int nRead;
			while ((nRead = ch.read(bb)) != -1) {
				bb.position(0);
				bb.limit(nRead);
				while (bb.hasRemaining()) {
					Event ev = new Event(bb.getInt(), lT = bb.getLong(),
							bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(),
							bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(),
							bb.getInt(), bb.getInt(), bb.getInt());

					if (startPlayTime == 0) {
						startPlayTime = ev.getTimestamp();
						startSystemTime = System.currentTimeMillis();
					}

					if (SEND_DATA_IN_PLAY_TIME) {
						long timeAhead = (((lT - startPlayTime) / 1000l / 1000l / 1000l) - (System
								.currentTimeMillis() - startSystemTime));
						if (timeAhead > 0)
							Thread.sleep(timeAhead);
					}

					synchronized (queue) {
						// Wait for continue (Queue should not cause
						// OutOfMemoryError!)
						// (lT - (queue.getFirst().getTimestamp() +
						// bufferLength) > 0

						while (queue.size() > MAX_EVENTS_PER_MESSAGE)
							queue.wait();

						// Append Event to queue
						queue.add(ev); // O(1)-op

						// Notify other Threads and wait
						queue.notifyAll();
					}
					// proxy.EventNotification("mobilis@sea/SEA", bb.getInt(),
					// bb.getLong(), bb.getInt(), bb.getInt(),
					// bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(),
					// bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(),
					// bb.getInt());
				}
				bb.clear();
			}
		} catch (IOException eIO) {
			eIO.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processQueue() {
		List<Event> eventsToSend;
		try {
			while (true) {
				synchronized (queue) {
					// Wait till we get first input!
					while (queue.size() < MIN_EVENTS_PER_MESSAGE)
						queue.wait();

					eventsToSend = new ArrayList<Event>();

					// Pop Events from list
					while (!queue.isEmpty()
							&& eventsToSend.size() < MAX_EVENTS_PER_MESSAGE)
						eventsToSend.add(queue.pop());

					// queue.wait(1000, 0);

					// Done => Notify
					queue.notifyAll();
				}

				// send
				proxy.EventNotification("mobilis@sea/SEA", eventsToSend);

				// following code for console output

				eventCounter += eventsToSend.size();
				messageCounter++;

				if (System.currentTimeMillis() - timeOfCycleStart > 1000) {

					long timeNeededInThisCycleInMS = System.currentTimeMillis()
							- timeOfCycleStart;
					int messagesPerSecond = (int) (messageCounter * 1000l / timeNeededInThisCycleInMS);
					int eventsPerSecond = (int) (eventCounter * 1000l / timeNeededInThisCycleInMS);
					long playtimeInMillisecs = (eventsToSend.get(0)
							.getTimestamp() - startPlayTime) / 1000 / 1000 / 1000;
					int playtimeInMinutesOnly = (int) (playtimeInMillisecs / 1000 / 60);
					int playtimeInSecondsOnly = (int) (playtimeInMillisecs / 1000 - (playtimeInMinutesOnly * 60));
					System.out
							.println("OUTGOING:  "
									+ playtimeInMinutesOnly
									+ "min "
									+ playtimeInSecondsOnly
									+ "s"
									+ " playtime"
									+ " | "
									+ messagesPerSecond
									+ " messages/s"
									+ " | "
									+ eventsPerSecond
									+ " events/s"
									+ " | "
									+ ((messagesPerSecond != 0) ? (eventsPerSecond / messagesPerSecond)
											: "-") + " events/message");

					timeOfCycleStart = System.currentTimeMillis();
					eventCounter = 0;
					messageCounter = 0;
				}

			}
		} catch (InterruptedException eIE) {
			// TODO Auto-generated catch block
			eIE.printStackTrace();
		}
	}

	private void processFile() {
		// Some auxiliary variables
		String e = null;
		long lT;

		try {
			rndAccFile.seek(0);
			// Loop over file (till no more line does exist)
			while ((e = rndAccFile.readLine()) != null) {
				// Read data of entry and set new timestamp
				Event ev = processEntry(e.split(","));
				lT = ev.getTimestamp();

				synchronized (queue) {
					// Wait for continue (Queue should not cause
					// OutOfMemoryError!)
					while (!queue.isEmpty()
							&& lT
									- (queue.getFirst().getTimestamp() + bufferLength) > 0) {
						queue.notifyAll();
						queue.wait();
					}

					// Append Event to queue
					queue.add(ev); // O(1)-op

					// Notify other Threads and wait
					queue.notifyAll();
				}
			}
		} catch (IOException | InterruptedException eIO) {
			// TODO: Some Exception-Handling
			eIO.printStackTrace();
		}
	}
}
