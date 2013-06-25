package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Map;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer.ReceptionListener;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.utils.AppendingObjectInputStream;

/**
 * The <code>RawReceiver</code> is used to receive <code>Datagram</code>s (UDP).
 * It deserializes the binary input to the original <code>Raw</code>
 * <code>Object</code>s and distributes those items to the registered
 * <code>ReceptionListener</code>s
 */
public class RawReceiver {

	/**
	 * Socket where to receive datagram packets
	 */
	private DatagramSocket socket;

	/**
	 * <code>Boolean</code> which declares whether this <code>RawReceiver</code>
	 * should receive data
	 */
	private boolean opened = true;

	/**
	 * <code>Boolean</code> to determine whether this <code>RawReceiver</code>
	 * should should receive packets
	 */
	private boolean receive = false;

	/**
	 * <code>Boolean</code> to determine whether this <code>RawReceiver</code>
	 * should sort the incoming packets (default: <code>false</code>)
	 */
	private boolean sorting = false;

	/**
	 * A <code>Map</code> of all payloadTypes which are allowed (those are the
	 * communication <code>Object</code>s of type <code>Raw</code>)
	 */
	private Map<Byte, Raw> knownPayloads;

	/**
	 * A <code>Map</code> of all registered listeners (this
	 * <code>RawReceiver</code> will use those listeners to distribute incoming
	 * <code>Raw</code>s)
	 */
	private Map<Byte, ReceptionListener> listeners;

	/**
	 * An internal queue to buffer incoming packets (is also used as mutex)
	 */
	private LinkedList<byte[]> packetQueue = new LinkedList<byte[]>();

	/**
	 * Constructor for a <code>RawReceiver</code>. This <code>RawReceiver</code>
	 * will instantly listen to the specified port!
	 * 
	 * @param payloadTypes
	 *            <code>Map</code> of all payloadTypes which are allowed (those
	 *            are the communication <code>Object</code>s of type
	 *            <code>Raw</code>)
	 * @param listeners
	 *            <code>Map</code> of all registered listeners (this
	 *            <code>RawReceiver</code> will use those listeners to
	 *            distribute incoming <code>Raw</code>s)
	 * @param localPort
	 *            the port on which this <code>RawReceiver</code> should listen
	 */
	public RawReceiver(Map<Byte, Raw> payloadTypes,
			Map<Byte, ReceptionListener> listeners, int localPort) {
		try {
			socket = new DatagramSocket(localPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.knownPayloads = payloadTypes;
		this.listeners = listeners;

		new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] buf = new byte[RawTransmitter.MAX_PAYLOAD_LENGTH];
				DatagramPacket p = new DatagramPacket(buf,
						RawTransmitter.MAX_PAYLOAD_LENGTH);
				byte[] b;

				while (opened) {
					try {
						// Receive new packet
						socket.receive(p);

						// Clean packetBuffer and save to new byte[]
						b = new byte[p.getLength()];
						System.arraycopy(buf, 0, b, 0, p.getLength());

						synchronized (packetQueue) {
							// Just add packet to queue (don't wait, if
							// queue has specific size)
							packetQueue.add(b);

							// Notify readers
							packetQueue.notifyAll();
						}
					} catch (IOException e) {
						// Ignore
					}
				}
			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				byte cpIDx = Byte.MIN_VALUE, pT;
				byte[] cP = null;
				ReceptionListener l;
				Raw r = null;

				while (opened) {
					if (receive) {
						synchronized (packetQueue) {
							// Wait till we get first input!
							while (packetQueue.isEmpty())
								try {
									packetQueue.wait();
								} catch (InterruptedException e) {
									// Ignore
								}

							// Take next packet
							if (sorting) {
								if (cP == null) {
									cP = packetQueue.pop();
									cpIDx = (byte) (cP[0] + 1);
								} else {
									for (int i = 0; i < packetQueue.size(); i++) {
										if (packetQueue.get(i)[0] == cpIDx) {
											cP = packetQueue.remove(i);
											cpIDx++;
											break;
										}
									}
								}
							} else {
								cP = packetQueue.pop();
							}

							// Done => Notify
							packetQueue.notifyAll();
						}

						try {
							ByteArrayInputStream bais = new ByteArrayInputStream(
									cP);
							// Skip packetID
							bais.skip(1);
							ObjectInputStream ois = new AppendingObjectInputStream(
									bais);

							while (bais.available() > 0) {
								pT = (byte) ois.read();
								l = RawReceiver.this.listeners.get(pT);
								(r = knownPayloads.get(pT)).readExternal(ois);

								if (l != null)
									l.handle(r.clone());
							}
						} catch (IOException | ClassNotFoundException e) {
							// Ignore (will just ignore this packet...)
						}
					}
				}
			}
		}).start();
	}

	/**
	 * This method should be invoked directly after this
	 * <code>RawReceiver</code> is created => may cause OutOfMemmoryError if not
	 * started. This <code>RawReceiver</code> has to start receiving immediately
	 * (due to NAT reasons!)
	 */
	public void start() {
		this.receive = true;
	}

	/**
	 * Sets whether this <code>RawReceiver</code> should try to sort incoming
	 * packets
	 * 
	 * @param sorting
	 *            <code>true</code> if the incoming <code>Datagram</code>s
	 *            should be sorted (otherwise <code>false</code>)
	 */
	public void setSorting(boolean sorting) {
		this.sorting = sorting;
	}

	/**
	 * Gets the <code>boolean</code> which determines whether this
	 * <code>RawReceiver</code> does sort the incoming packets
	 * 
	 * @return <code>true</code> if the incoming <code>Datagram</code>s are
	 *         sorted (otherwise <code>false</code>)
	 */
	public boolean getSorting() {
		return sorting;
	}

	/**
	 * Stop receiving
	 */
	public void stop() {
		this.receive = false;
		this.opened = false;
		socket.close();
	}

}
