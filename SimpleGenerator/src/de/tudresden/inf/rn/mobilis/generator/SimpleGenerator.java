package de.tudresden.inf.rn.mobilis.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

import de.tudresden.inf.rn.mobilis.generator.interfaces.Event;
import de.tudresden.inf.rn.mobilis.generator.interfaces.SEADispatcher;

public class SimpleGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SimpleGenerator("X:\\full-game", new SEADispatcher() {
			private int i = 0;
			@Override
			public void onEventNotification(Event in) {
				i++;
				if (i % 100000 == 0)
					System.out.println(i + " Events processed");
			}
		}, 5l);
	}

	/**
	 * Queue for <code>Event</code>'s (also used as mutex)
	 */
	private LinkedList<Event> queue;

	/**
	 * Size of the buffer (in ms => to avoid <code>OutOfMemmoryError</code>)
	 */
	private long bufferLength;

	public SimpleGenerator(final String inFile, final SEADispatcher dispatcher,
			long bufferLength) {
		this.bufferLength = bufferLength * 1000 * 1000 * 1000;
		queue = new LinkedList<Event>();
		Thread p = new Thread() {
			public void run() {
				processData(inFile);
			}
		};
		p.setPriority(Thread.MAX_PRIORITY);
		Thread c = new Thread() {
			public void run() {
				processQueue(dispatcher);
			}
		};
		c.setPriority(Thread.MAX_PRIORITY);
		p.start();
		c.start();
	}

	private void processData(String inFile) {
		try {
			FileChannel iCh = new FileInputStream(inFile).getChannel();
			ByteBuffer iBb = ByteBuffer.allocateDirect(262144);

			char[] cA = new char[19];
			short c = 0;
			int eC = 0, nR;
			byte bR;
			long lT;
			String[] b = new String[13];
			Event ev;
			while ((nR = iCh.read(iBb)) != -1) {
				iBb.position(0);
				iBb.limit(nR);
				while (iBb.hasRemaining()) {
					if ((bR = iBb.get()) == 13) {
						// NOOP
					} else if (bR == 10) {
						b[eC] = String.valueOf(cA, 0, c);
						ev = new Event(Integer.valueOf(b[0]),
								lT = Long.valueOf(b[1]), Integer.valueOf(b[2]),
								Integer.valueOf(b[3]), Integer.valueOf(b[4]),
								Integer.valueOf(b[5]), Integer.valueOf(b[6]),
								Integer.valueOf(b[7]), Integer.valueOf(b[8]),
								Integer.valueOf(b[9]), Integer.valueOf(b[10]),
								Integer.valueOf(b[11]), Integer.valueOf(b[12]));
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
						c = 0;
						eC = 0;
					} else if (bR == 44) {
						b[eC] = String.valueOf(cA, 0, c);
						c = 0;
						eC++;
					} else {
						cA[c] = (char) bR;
						c++;
					}
				}
				iBb.clear();
			}
			iCh.close();
		} catch (IOException eIO) {
			eIO.printStackTrace();
		} catch (InterruptedException eIE) {
			eIE.printStackTrace();
		}

	}

	private void processQueue(SEADispatcher dispatcher) {
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
				dispatcher.onEventNotification(ev);
			}
		} catch (InterruptedException eIE) {
			eIE.printStackTrace();
		}
	}
}
