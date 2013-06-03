package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.utils.AppendingObjectOutputStream;

public class RawTransmitter implements Runnable {

	public final static int MAX_PAYLOAD_LENGTH = 1024;

	/**
	 * Socket to send datagram packets
	 */
	private DatagramSocket socket;

	/**
	 * IP address (remote)
	 */
	private InetAddress remoteHost;

	/**
	 * Port (remote)
	 */
	private int remotePort;

	/**
	 * <code>Boolean</code> to determine whether this
	 * <code>RawTransmitter</code> should transmit
	 */
	private boolean transmit = false;

	/**
	 * <code>Boolean</code> which declares whether this RawTransmitter should
	 * send data
	 */
	private boolean opened = true;

	/**
	 * Queue which holds <code>Object</code> to be send
	 */
	private LinkedBlockingQueue<Raw> queue;

	public RawTransmitter(LinkedBlockingQueue<Raw> queue,
			DatagramSocket socket, InetAddress remoteHost, int remotePort) {
		this.queue = queue;
		this.socket = socket;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.transmit = true;
	}

	/**
	 * Start to transmit
	 */
	public void start() {
		byte packetIDx = 0;
		opened = true;
		byte[] buffer = new byte[RawTransmitter.MAX_PAYLOAD_LENGTH];
		final DatagramPacket p = new DatagramPacket(buffer,
				RawTransmitter.MAX_PAYLOAD_LENGTH, remoteHost, remotePort);

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream oos;
			Raw e;
			// boolean single = true;
			baos.write(packetIDx);
			while (opened) {
				// Reset buffer
				b.reset();
				oos = new AppendingObjectOutputStream(b);

				// Blocking take! (=> This may block when no items arrive!)
				e = queue.take();

				// Actually write new payload into buffer
				oos.writeByte(e.getPayloadType());
				e.writeExternal(oos);
				// System.out.println(baos.size());

				if (transmit
						&& (queue.isEmpty() || (baos.size() + b.size() > RawTransmitter.MAX_PAYLOAD_LENGTH))) {
					// Finally send
					buffer = baos.toByteArray();
					p.setData(buffer);
					p.setLength(buffer.length);

					socket.send(p);

					// Reset buffer
					baos.reset();

					// Write new packetID
					packetIDx++;
					// System.out.println(packetIDx);
					baos.write(packetIDx);
				}
				oos.flush();
				oos.close();
				baos.write(b.toByteArray());
			}
		} catch (IOException | InterruptedException eX) {
			eX.printStackTrace();
		}
	}

	@Override
	public void run() {
		start();
	}

	/**
	 * Set transmit enabled/disabled
	 * 
	 * @param transmit
	 *            boolean to enable/disable transmission
	 */
	public void setTransmit(boolean transmit) {
		this.transmit = transmit;
	}

	/**
	 * Stop transmitting
	 */
	public void stop() {
		this.transmit = false;
		this.opened = false;
		socket.close();
	}
}
