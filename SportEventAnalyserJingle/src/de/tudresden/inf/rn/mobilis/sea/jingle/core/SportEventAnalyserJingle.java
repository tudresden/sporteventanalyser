package de.tudresden.inf.rn.mobilis.sea.jingle.core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.jingle.JingleManager;
import org.jivesoftware.smackx.jingle.JingleSession;
import org.jivesoftware.smackx.jingle.JingleSessionRequest;
import org.jivesoftware.smackx.jingle.listeners.JingleSessionRequestListener;
import org.jivesoftware.smackx.jingle.media.JingleMediaManager;
import org.jivesoftware.smackx.jingle.nat.BasicTransportManager;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.RawMediaManager;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer.ReceptionListener;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.InterruptionBegin;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.InterruptionEnd;

/**
 * The <code>SportEventAnalyserJingle</code> may be used to establish a
 * jingle-connection to a remote host or receive incoming <code>Raw</code>s
 */
public class SportEventAnalyserJingle {

	/**
	 * The <code>smackx.jingle.JingleManager</code> which provides the bulk of
	 * functionality as defined in the jingle specification XEP-0166
	 */
	private final JingleManager jM;

	/**
	 * The internal <code>RawMediaManager</code> which does create
	 * <code>RawMediaSession</code>s for all payloads of type 42 ("raw")
	 */
	private final RawMediaManager rMM;

	/**
	 * Constructor for a <code>SportEventAnalyserJingle</code>. This class may
	 * be used to establish a jingle-connection to a remote host or receive
	 * incoming <code>Raw</code>s
	 * 
	 * @param connection
	 *            the <code>smack.Connection</code> which is used to communicate
	 *            with the XMPP-Server
	 */
	public SportEventAnalyserJingle(Connection connection) {
		// Enable Jingle
		JingleManager.setJingleServiceEnabled();

		// Create STUN-Transport-Manager (have a look at stun-config.xml) -> May
		// also be a ICETransportManager (using TURN => Do you have a
		// TURN-Server?)
		// STUNTransportManager stunMgr = new STUNTransportManager();
		// ICETransportManager stunMgr = new ICETransportManager(connection,
		// "stun.l.google.com", 19302);
		BasicTransportManager tMgr = new BasicTransportManager();

		// Append media manager for raw types
		List<JingleMediaManager> mediaManagers = new LinkedList<JingleMediaManager>();
		mediaManagers.add(rMM = new RawMediaManager(tMgr));

		jM = new JingleManager(connection, mediaManagers);
		registerPayloads();

		jM.addJingleSessionRequestListener(new JingleSessionRequestListener() {

			@Override
			public void sessionRequested(JingleSessionRequest request) {
				try {
					JingleSession incoming = request.accept();

					incoming.startIncoming();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * Registers all known payloads (this has to be done for each item which is
	 * sent! Any unregistered payloads may cause corrupt data)
	 */
	private void registerPayloads() {
		rMM.registerPayload(new Event());
		rMM.registerPayload(new InterruptionBegin());
		rMM.registerPayload(new InterruptionEnd());
	}

	/**
	 * Establish a <code>SportEventAnalyserJingle</code>-session with
	 * corresponding <code>hostJID</code>
	 * 
	 * @param hostJID
	 *            the JID which is used as host
	 * @return <code>LinkedBlockingQueue<Raw></code> which may be used to append
	 *         payload
	 * @throws XMPPException
	 */
	public LinkedBlockingQueue<Raw> establishConnection(String hostJID)
			throws XMPPException {
		JingleSession js = jM.createOutgoingJingleSession(hostJID);
		js.startOutgoing();
		return rMM.getSession().getTransmissionQueue();
	}

	/**
	 * Sets a new listener for an incoming payload type. You may only register
	 * one <code>ReceptionListener</code> per payload type!
	 * 
	 * @param payloadType
	 *            the type of the payload
	 * @param listener
	 *            a <code>ReceptionListener</code>
	 */
	public void setReceptionListener(byte payloadType,
			ReceptionListener listener) {
		rMM.registerListener(payloadType, listener);
	}

}
