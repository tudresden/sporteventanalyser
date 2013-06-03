package de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.RawMediaSession;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;

public class SessionObserver {

	/**
	 * <code>List</code> of registered <code>Raw</code> prototypes. The
	 * prototypes will be set as known prototypes to the
	 * <code>RawMediaSession</code>
	 */
	private List<Raw> prototypes;

	/**
	 * <code>List</code> of registered <code>ReceptionListener</code>. The
	 * listeners will be added as listeners to the <code>RawMediaSession</code>
	 */
	private Map<Byte, ReceptionListener> listeners;

	/**
	 * The session where prototypes and listeners should be added
	 */
	private RawMediaSession session = null;

	public SessionObserver() {
		prototypes = new LinkedList<Raw>();
		listeners = new HashMap<Byte, ReceptionListener>();
	}

	/**
	 * Registers a prototype (will be automatically added to known prototypes of
	 * the session)
	 * 
	 * @param prototype
	 *            the <code>Raw</code> prototype which should be added
	 */
	public void registerPayload(Raw prototype) {
		prototypes.add(prototype);
		if (session != null)
			session.registerPayload(prototype);
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
		listeners.put(payloadType, listener);
		if (session != null)
			session.addListener(payloadType, listener);
	}

	/**
	 * Sets the <code>RawMediaSession</code> and add registered <code>Raw</code>
	 * prototypes and listeners
	 * 
	 * @param session
	 *            the created <code>RawMediaSession</code>
	 */
	public void setSession(RawMediaSession session) {
		this.session = session;

		// Register payloads
		for (Raw prototype : prototypes)
			session.registerPayload(prototype);

		// Register listeners
		byte payloadType;
		for (Iterator<Byte> payloadTypeIt = listeners.keySet().iterator(); payloadTypeIt
				.hasNext();) {
			payloadType = payloadTypeIt.next();
			session.addListener(payloadType, listeners.get(payloadType));
		}

		// Call immediately after this method has finished setting up all
		// listeners and prototypes
		session.startReceive();
	}

	/**
	 * Get the <code>RawMediaSession</code> which is observed (may be null, if
	 * no session has been set yet!)
	 * 
	 * @return the <code>RawMediaSession</code> which is observed
	 */
	public RawMediaSession getSession() {
		return session;
	}

}
