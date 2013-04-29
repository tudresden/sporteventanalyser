package de.tudresden.inf.rn.inflator.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

import de.tudresden.inf.rn.mobilis.sea.client.proxy.Event;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.SportEventAnalyserProxy;

public class Reader {

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
					synchronized (queue) {
						// Wait for continue (Queue should not cause
						// OutOfMemoryError!)
						while (!queue.isEmpty()
								&& lT
										- (queue.getFirst().getTimestamp() + bufferLength) > 0)
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
		Event ev;
		try {
			while (true) {
				synchronized (queue) {
					// Wait till we get first input!
					while (queue.isEmpty())
						queue.wait();

					// Pop Event from list
					ev = queue.pop(); // O(1)-op

					// Done => Notify
					queue.notifyAll();
				}

				// Process Event
				proxy.EventNotification("mobilis@sea/SEA", ev.getSender(),
						ev.getTimestamp(), ev.getPositionX(),
						ev.getPositionY(), ev.getPositionZ(), ev.getVelocity(),
						ev.getAcceleration(), ev.getVelocityX(),
						ev.getVelocityY(), ev.getVelocityZ(),
						ev.getAccelerationX(), ev.getAccelerationY(),
						ev.getAccelerationZ());
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
