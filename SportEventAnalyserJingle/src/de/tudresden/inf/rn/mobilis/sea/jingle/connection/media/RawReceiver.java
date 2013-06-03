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

public class RawReceiver {

	/**
	 * Socket where to receive datagram packets
	 */
	private DatagramSocket socket;

	private boolean opened = true;

	private boolean receive = false;

	private Map<Byte, Raw> knownPayloads;

	private Map<Byte, ReceptionListener> listeners;

	private LinkedList<byte[]> packetQueue = new LinkedList<byte[]>();

	/**
	 * Constructor for a <code>RawReceiver</code>. This <code>RawReceiver</code>
	 * will instantly listen to the specified port!
	 * 
	 * @param localPort
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
							if (cP == null) {
								cP = packetQueue.pop();
								cpIDx = (byte) (cP[0] + 1);
							} else {
								for (int i = 0; i < packetQueue.size(); i++) {
									if (packetQueue.get(i)[0] == cpIDx) {
										cP = packetQueue.remove(i);
										cpIDx++;
										// TODO: This may still not "sort"
										// packets! Es muss auch ueberprueft
										// werden, dass ueberhaupt ein Paket
										// gefunden wurde!
										break;
									}
								}
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
	 * Stop receiving
	 */
	public void stop() {
		this.receive = false;
		this.opened = false;
		socket.close();
	}

}
