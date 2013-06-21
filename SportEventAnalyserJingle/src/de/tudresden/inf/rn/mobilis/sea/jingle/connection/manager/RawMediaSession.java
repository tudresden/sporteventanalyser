package de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.jivesoftware.smackx.jingle.JingleSession;
import org.jivesoftware.smackx.jingle.media.JingleMediaSession;
import org.jivesoftware.smackx.jingle.media.PayloadType;
import org.jivesoftware.smackx.jingle.nat.TransportCandidate;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer.ReceptionListener;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.RawReceiver;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.RawTransmitter;

public class RawMediaSession extends JingleMediaSession {

	private RawTransmitter transmitter = null;

	private LinkedBlockingQueue<Raw> transmissionQueue = null;

	private RawReceiver receiver = null;

	private Map<Byte, Raw> payloadTypes = null;

	private Map<Byte, ReceptionListener> listeners = null;

	public RawMediaSession(PayloadType payloadType, TransportCandidate remote,
			TransportCandidate local, String mediaLocator,
			JingleSession jingleSession) {
		super(payloadType, remote, local, mediaLocator, jingleSession);
		initialize();
	}

	@Override
	public void initialize() {
		JingleSession s = getJingleSession();
		if (s != null && s.getInitiator().equals(s.getConnection().getUser())) {
			// Create transmitter
			try {
				transmitter = new RawTransmitter(
						transmissionQueue = new LinkedBlockingQueue<Raw>(100),
						new DatagramSocket(getLocal().getPort()),
						InetAddress.getByName(getRemote().getIp()), getRemote()
								.getPort());
			} catch (SocketException | UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			// Create new map where all known payload types are registered
			payloadTypes = new ConcurrentHashMap<Byte, Raw>();

			// Create new map where all listeners are registered
			listeners = new ConcurrentHashMap<Byte, ReceptionListener>();

			// Create receiver
			receiver = new RawReceiver(payloadTypes, listeners, getLocal()
					.getPort());
		}
	}

	/**
	 * Get the transmission queue (the queue will be <code>null</code>, if no
	 * transmitter has been created yet)
	 * 
	 * @return the transmission queue
	 */
	public LinkedBlockingQueue<Raw> getTransmissionQueue() {
		return this.transmissionQueue;
	}

	/**
	 * Registers a concrete <code>Raw</code> payload
	 * 
	 * @param payloadType
	 *            a prototype of the <code>Raw</code> payload
	 */
	public void registerPayload(Raw payloadType) {
		if (payloadTypes != null)
			payloadTypes.put(payloadType.getPayloadType(), payloadType);
	}

	public void addListener(byte payloadType, ReceptionListener listener) {
		if (listeners != null)
			listeners.put(payloadType, listener);
	}

	@Override
	public void setTrasmit(boolean active) {
		if (transmitter != null)
			transmitter.setTransmit(active);
	}

	@Override
	public void startReceive() {
		if (receiver != null)
			receiver.start();
	}

	@Override
	public void startTrasmit() {
		new Thread(transmitter).start();
	}

	@Override
	public void stopReceive() {
		if (receiver != null)
			receiver.stop();
	}

	@Override
	public void stopTrasmit() {
		if (transmitter != null) {
			transmitter.stop();
		}
	}

}
