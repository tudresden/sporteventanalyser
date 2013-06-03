package de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager;

import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.smackx.jingle.JingleSession;
import org.jivesoftware.smackx.jingle.media.JingleMediaManager;
import org.jivesoftware.smackx.jingle.media.JingleMediaSession;
import org.jivesoftware.smackx.jingle.media.PayloadType;
import org.jivesoftware.smackx.jingle.nat.JingleTransportManager;
import org.jivesoftware.smackx.jingle.nat.TransportCandidate;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer.ReceptionListener;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer.SessionObserver;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;

public class RawMediaManager extends JingleMediaManager {

	/**
	 * List of supported payloads
	 */
	private List<PayloadType> payloads = new LinkedList<PayloadType>();

	/**
	 * An auxiliary <code>SessionObserver</code> which is used to immediately
	 * register known payloads when a <code>RawMediaSession</code> has been
	 * created
	 */
	private SessionObserver sessionObserver;

	public RawMediaManager(JingleTransportManager transportManager) {
		super(transportManager);
		setupPayloads();

		sessionObserver = new SessionObserver();
	}

	/**
	 * Setup supported <code>PayloadType</code>s
	 */
	private void setupPayloads() {
		payloads.add(new PayloadType.Audio(42, "raw"));
	}

	@Override
	public List<PayloadType> getPayloads() {
		return payloads;
	}

	@Override
	public JingleMediaSession createMediaSession(PayloadType payloadType,
			TransportCandidate remote, TransportCandidate local,
			JingleSession session) {
		// Set session
		sessionObserver.setSession(new RawMediaSession(payloadType, remote,
				local, "Raw", session));
		return sessionObserver.getSession();
	}

	/**
	 * Get the <code>RawMediaSession</code> which has been created
	 * 
	 * @return the <code>RawMediaSession</code>
	 */
	public RawMediaSession getSession() {
		while (sessionObserver.getSession() == null) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// Ignore
			}
		}
		return sessionObserver.getSession();
	}

	/**
	 * Registers a prototype (will be automatically added to known prototypes of
	 * the session)
	 * 
	 * @param prototype
	 *            the <code>Raw</code> prototype which should be added
	 */
	public void registerPayload(Raw prototype) {
		sessionObserver.registerPayload(prototype);
	}

	/**
	 * Register a new listener for an incoming payload type. You may only
	 * register one <code>ReceptionListener</code> per payload type!
	 * 
	 * @param payloadType
	 *            the type of the payload
	 * @param listener
	 *            a <code>ReceptionListener</code>
	 */
	public void registerListener(byte payloadType, ReceptionListener listener) {
		sessionObserver.registerListener(payloadType, listener);
	}
}
