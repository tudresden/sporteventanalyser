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

/**
 * This <code>RawMediaSession</code> extends the <code>JingleMediaSession</code>
 * to create a session (it decides itself whether this
 * <code>RawMediaSession</code> should transmit or receive data)
 */
public class RawMediaSession extends JingleMediaSession {

	/**
	 * A <code>RawTransmitter</code> to send data
	 */
	private RawTransmitter transmitter = null;

	/**
	 * This is an auxiliary queue to append <code>Raw</code>s which should be
	 * sent
	 */
	private LinkedBlockingQueue<Raw> transmissionQueue = null;

	/**
	 * A <code>RawReceiver</code> which may receive data
	 */
	private RawReceiver receiver = null;

	/**
	 * A <code>Map</code> of all payloadTypes which are allowed (those are the
	 * communication <code>Object</code>s of type <code>Raw</code>)
	 */
	private Map<Byte, Raw> payloadTypes = null;

	/**
	 * A <code>Map</code> of all registered listeners (the receiver will use
	 * those listeners to distribute incoming <code>Raw</code>s)
	 */
	private Map<Byte, ReceptionListener> listeners = null;

	/**
	 * Constructor for a <code>RawMediaSession</code>
	 * 
	 * @param payloadType
	 *            the payloadType (which should be of type 42 ("raw")
	 * @param remote
	 *            the chosen <code>TransportCandidate</code> for the remote
	 *            device
	 * @param local
	 *            the chosen <code>TransportCandidate</code> for the local
	 *            device
	 * @param mediaLocator
	 *            an auxiliary mediaLocator-<code>String</code>
	 * @param jingleSession
	 *            the concrete <code>JingleSession</code> which has been created
	 */
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
