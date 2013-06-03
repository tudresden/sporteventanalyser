package de.tudresden.inf.rn.inflator.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.InterruptionBegin;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.InterruptionEnd;

public class Reader {

	private static final int MAX_EVENTS_IN_QUEUE = 500;
	private static final boolean SEND_DATA_IN_PLAY_TIME = true;
	private static int eventCounter = 0;
	private static long startPlayTime = 0, startSystemTime = 0;
	private static long timeOfCycleStart = System.currentTimeMillis();

	/**
	 * Queue for <code>Event</code>'s (also used as mutex)
	 */
	private LinkedList<Raw> queue;

	private FileInputStream f;

	private RandomAccessFile raf;

	private LinkedBlockingQueue<Raw> rawQueue;

	private boolean running = false;

	public Reader(File sensorData, File interruptionData,
			LinkedBlockingQueue<Raw> rawQueue) throws FileNotFoundException {
		f = new FileInputStream(sensorData);
		raf = new RandomAccessFile(interruptionData, "r");

		this.queue = new LinkedList<Raw>();
		this.rawQueue = rawQueue;

		// Process actions in new Threads
		Thread readerSensor = new Thread() {
			public void run() {
				processTransformedData();
			}
		};
		readerSensor.setPriority(Thread.MAX_PRIORITY);
		Thread processor = new Thread() {
			public void run() {
				processQueue();
			}
		};
		processor.setPriority(Thread.MAX_PRIORITY);
		new Thread() {
			public void run() {
				processInterruptionData();
			}
		}.start();
		readerSensor.start();
		processor.start();
	}

	public void start() {
		running = true;
	}

	public void stop() {
		running = false;
	}

	private synchronized void setStartPlayTime(long time) {
		if (startPlayTime == 0) {
			startPlayTime = time;
		}
	}

	private synchronized void setStartSystemTime() {
		if (startSystemTime == 0) {
			startSystemTime = System.currentTimeMillis();
		}
	}

	private long convertRealTimeToPicoSeconds(String real) {
		String[] hms = real.split(":");
		return Long.valueOf(hms[0]) * 60l * 60l * 1000l * 1000l * 1000l * 1000l
				+ Long.valueOf(hms[1]) * 60l * 1000l * 1000l * 1000l * 1000l
				+ Long.valueOf(hms[2].replace(".", "")) * 1000l * 1000l * 1000l;
	}

	@SuppressWarnings("null")
	private void processInterruptionData() {
		String content;
		String[] iTime;
		long begin = 0l, end = 0l, tA;
		InterruptionBegin iB, hE = null;
		InterruptionEnd iE, hB;
		try {
			while (true) {
				if (running) {
					while ((content = raf.readLine()) != null) {
						iTime = content.split(",");
						if (iTime[0].equals("begin")) {
							setStartPlayTime(begin = Long.valueOf(iTime[1]
									.replace("end(", "")));
							end = Long.valueOf(iTime[2].replace(")", ""));
							if (hE != null) {
								// Second half "begun" (does not really begin:
								// Just read second half mark) -> Send
								// InterruptionBegin
								if (SEND_DATA_IN_PLAY_TIME) {
									if ((tA = (((hE.getBegin() - startPlayTime) / 1000l / 1000l / 1000l) - (System
											.currentTimeMillis() - startSystemTime))) > 0) {
										Thread.sleep(tA);
									}
								}
								// Send InterruptionBegin
								synchronized (queue) {
									// Just add InterruptionBegin as first
									// element
									// Don't wait for queue (will not cause OOM)
									// -> Prior Interruptions!
									queue.addFirst(hE);

									// Notify other Threads
									queue.notifyAll();
								}
								System.out.println("break");

								// Set InterruptionEnd (Second half starts)
								hB = new InterruptionEnd(begin);

								if (SEND_DATA_IN_PLAY_TIME) {
									if ((tA = (((hB.getEnd() - startPlayTime) / 1000l / 1000l / 1000l) - (System
											.currentTimeMillis() - startSystemTime))) > 0) {
										Thread.sleep(tA);
									}
								}
								// Send InterruptionEnd
								synchronized (queue) {
									// Just add InterruptionEnd as first element
									// Don't wait for queue (will not cause OOM)
									// ->
									// Prior Interruptions!
									queue.addFirst(hB);

									// Notify other Threads
									queue.notifyAll();
								}
								System.out.println("second");
							}
							hE = new InterruptionBegin(end);
						} else {
							iB = new InterruptionBegin(
									convertRealTimeToPicoSeconds(iTime[0])
											+ begin);
							iE = new InterruptionEnd(
									convertRealTimeToPicoSeconds(iTime[1])
											+ begin);

							synchronized (queue) {
								// Wait till we may process interruptions
								while (startSystemTime == 0)
									queue.wait();

								// Notify
								queue.notifyAll();
							}
							if (SEND_DATA_IN_PLAY_TIME) {
								if ((tA = (((iB.getBegin() - startPlayTime) / 1000l / 1000l / 1000l) - (System
										.currentTimeMillis() - startSystemTime))) > 0) {
									Thread.sleep(tA);
								}
							}
							// Send InterruptionBegin
							synchronized (queue) {
								// Just add InterruptionBegin as first element
								// Don't wait for queue (will not cause OOM) ->
								// Prior Interruptions!
								queue.addFirst(iB);

								// Notify other Threads
								queue.notifyAll();
							}

							if (SEND_DATA_IN_PLAY_TIME) {
								if ((tA = (((iE.getEnd() - startPlayTime) / 1000l / 1000l / 1000l) - (System
										.currentTimeMillis() - startSystemTime))) > 0) {
									Thread.sleep(tA);
								}
							}

							// Send InterruptionEnd
							synchronized (queue) {
								// Just add InterruptionEnd as first element
								// Don't wait for queue (will not cause OOM) ->
								// Prior Interruptions!
								if (!queue.isEmpty()
										&& queue.getFirst().equals(iB))
									queue.add(1, iE);
								else
									queue.addFirst(iE);

								// Notify other Threads
								queue.notifyAll();
							}
						}
					}
					// Send InterruptionBegin (game ended)
					if (SEND_DATA_IN_PLAY_TIME) {
						if ((tA = (((hE.getBegin() - startPlayTime) / 1000l / 1000l / 1000l) - (System
								.currentTimeMillis() - startSystemTime))) > 0) {
							Thread.sleep(tA);
						}
					}
					// Send InterruptionBegin
					synchronized (queue) {
						// Just add InterruptionBegin as first element
						// Don't wait for queue (will not cause OOM) ->
						// Prior Interruptions!
						queue.addFirst(hE);

						// Notify other Threads
						queue.notifyAll();
					}

					// Close down this Thread
					break;
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void processTransformedData() {
		try {
			long lT, tA;

			FileChannel ch = f.getChannel();
			ByteBuffer bb = ByteBuffer.allocateDirect(262136);
			int nRead;
			while (true) {
				if (running) {
					while ((nRead = ch.read(bb)) != -1) {
						bb.position(0);
						bb.limit(nRead);
						while (bb.hasRemaining()) {
							Event ev = new Event(bb.getInt(),
									lT = bb.getLong(), bb.getInt(),
									bb.getInt(), bb.getInt(), bb.getInt(),
									bb.getInt(), bb.getInt(), bb.getInt(),
									bb.getInt(), bb.getInt(), bb.getInt(),
									bb.getInt());

							// Events are created before game starts! (filter)
							if (ev.getTimestamp() >= startPlayTime) {
								setStartSystemTime();

								if (SEND_DATA_IN_PLAY_TIME) {
									if ((tA = (((lT - startPlayTime) / 1000l / 1000l / 1000l) - (System
											.currentTimeMillis() - startSystemTime))) > 0)
										Thread.sleep(tA);
								}

								synchronized (queue) {
									// Wait for continue (Queue should not cause
									// OutOfMemoryError!)
									while (queue.size() > MAX_EVENTS_IN_QUEUE)
										queue.wait();

									// Append Event to queue
									queue.add(ev); // O(1)-op

									// Notify other Threads
									queue.notifyAll();
								}
							}
						}
						bb.clear();
					}
				}
			}
		} catch (IOException eIO) {
			eIO.printStackTrace();
		} catch (InterruptedException eI) {
			eI.printStackTrace();
		}

	}

	private void processQueue() {
		Raw e;
		try {
			while (true) {
				if (running) {
					synchronized (queue) {
						// Wait till we get first input!
						while (queue.isEmpty())
							queue.wait();

						// Pop Event from list
						e = queue.pop(); // O(1)-op

						// Done => Notify
						queue.notifyAll();
					}

					// Send
					rawQueue.put(e);

					// following code for console output
					eventCounter++;

					if (System.currentTimeMillis() - timeOfCycleStart > 1000) {
						//TODO: Delete this (Verursacht eh Fehler, sobald zufaelligerweise ein InterruptionBegin/End ist^^

						long timeNeededInThisCycleInMS = System
								.currentTimeMillis() - timeOfCycleStart;
						int eventsPerSecond = (int) (eventCounter * 1000l / timeNeededInThisCycleInMS);
						long playtimeInMillisecs = (((Event) e).getTimestamp() - startPlayTime) / 1000 / 1000 / 1000;
						int playtimeInMinutesOnly = (int) (playtimeInMillisecs / 1000 / 60);
						int playtimeInSecondsOnly = (int) (playtimeInMillisecs / 1000 - (playtimeInMinutesOnly * 60));
						System.out.println("OUTGOING:  "
								+ playtimeInMinutesOnly + "min "
								+ playtimeInSecondsOnly + "s" + " playtime"
								+ " | " + eventsPerSecond + " events/s");

						timeOfCycleStart = System.currentTimeMillis();
						eventCounter = 0;
					}
				}
			}
		} catch (InterruptedException eI) {
			eI.printStackTrace();
		}
	}

}
