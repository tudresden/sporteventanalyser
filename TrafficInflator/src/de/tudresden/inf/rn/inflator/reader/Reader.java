package de.tudresden.inf.rn.inflator.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

import de.tudresden.inf.rn.mobilis.sea.client.proxy.Event;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.SportEventAnalyserProxy;

public class Reader {

	/**
	 * Mutex for Queue
	 */
	private Object queueMutex = new Object();

	private LinkedList<Event> queue;

	/**
	 * The <code>RandomAccessFile</code> to read from
	 */
	private RandomAccessFile rndAccFile;

	private long bufferLength;

	private SportEventAnalyserProxy proxy;

	public Reader(File file, long bufferLength, SportEventAnalyserProxy proxy)
			throws FileNotFoundException {
		rndAccFile = new RandomAccessFile(file, "r");
		// BufferLength should be passed as ms, but data is passed in
		// picoseconds
		this.bufferLength = bufferLength * 1000 * 1000 * 1000;
		queue = new LinkedList<Event>();

		this.proxy = proxy;

		// Process actions in new Threads
		Thread reader = new Thread() {
			public void run() {
				processFile();
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

				synchronized (queueMutex) {
					// Append Event to queue
					queue.addLast(ev);

					// Notify other Threads and wait
					queueMutex.notifyAll();

					// Wait for continue (Queue should not cause
					// OutOfMemoryError!)
					while (!queue.isEmpty()
							&& lT
									- (queue.getFirst().getTimestamp() + bufferLength) < 0) {
						queueMutex.wait();
					}
				}
			}
		} catch (IOException | InterruptedException eIO) {
			// TODO: Some Exception-Handling
			eIO.printStackTrace();
		}
	}

	private void processQueue() {
		Event ev;
		try {
			while (true) {
				synchronized (queueMutex) {
					// Wait till we get first input!
					while (queue.isEmpty())
						queueMutex.wait();

					// Pop Event from list
					ev = queue.pop();

					// Done => Notify
					queueMutex.notifyAll();
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
}
