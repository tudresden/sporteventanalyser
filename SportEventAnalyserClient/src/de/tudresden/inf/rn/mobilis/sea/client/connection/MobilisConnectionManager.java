package de.tudresden.inf.rn.mobilis.sea.client.connection;

import java.util.concurrent.LinkedBlockingQueue;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;

import de.tudresden.inf.rn.mobilis.sea.client.connection.listener.IQListener;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.SportEventAnalyserProxy;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.impl.SEADispatcher;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.impl.SEADistributer;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;
import de.tudresden.inf.rn.mobilis.sea.jingle.core.SportEventAnalyserJingle;

/**
 * The <code>MobilisConnectionManager</code> is used to connect to the
 * Mobilis-Server using the smack library
 */
public class MobilisConnectionManager extends SEADistributer {

	/**
	 * This is the auxiliary <code>SportEventAnalyserJingle</code> which may be
	 * use to use the bulk of functionality which is provided by XEP-0166
	 * (Jingle)
	 */
	private SportEventAnalyserJingle seaJingle;

	/**
	 * Default constructor for a <code>MobilisConnectionManager</code> (Debug
	 * disabled)
	 */
	public MobilisConnectionManager() {
		// Default: false
		XMPPConnection.DEBUG_ENABLED = false;
	}

	/**
	 * Constructor for a <code>MobilisConnectionManager</code> where you may set
	 * whether debug-mode of smack should be enabled or not
	 * 
	 * @param debugEnabled
	 *            <code>true</code> if debug should be enabled (otherwise
	 *            <code>false</code>)
	 */
	public MobilisConnectionManager(boolean debugEnabled) {
		XMPPConnection.DEBUG_ENABLED = debugEnabled;
	}

	@Override
	public boolean connect(String server, int port, String service) {
		if (service == null || service.equals(""))
			connection = new XMPPConnection(new ConnectionConfiguration(server,
					port));
		else
			connection = new XMPPConnection(new ConnectionConfiguration(server,
					port, service));
		try {
			connection.connect();
		} catch (XMPPException e) {
			System.out.println("Error. Could not connect to XMPP-Server");
			return false;
		}

		System.out.println("Connected to: " + connection.getHost());
		return true;
	}

	@Override
	public boolean performLogin(String username, String password,
			String ressource, String toJID) {
		if (connection != null && connection.isConnected()) {
			try {
				this.registerXMPPExtensions();
				connection.addPacketListener(new IQListener(new SEADispatcher(
						new SportEventAnalyserProxy(this))),
						new PacketTypeFilter(IQ.class));
				connection.login(username, password, ressource);
			} catch (XMPPException e) {
				System.out.println("Error. Could not login to XMPP-Server!");
				return false;
			}

			// Send presence:
			Presence presence = new Presence(Presence.Type.available);
			presence.setTo(toJID);
			presence.setFrom(connection.getUser());
			connection.sendPacket(presence);

			System.out.println("Logged in on server " + connection.getHost()
					+ " as user: " + username);
			return true;
		}
		return false;
	}

	/**
	 * Establish a <code>SportEventAnalyserJingle</code>-session with
	 * corresponding <code>toJID</code>
	 * 
	 * @param toJID
	 *            the JID which is used as host
	 * @return <code>LinkedBlockingQueue<Raw<?>></code> which may be used to
	 *         append payload
	 * @throws XMPPException
	 */
	public LinkedBlockingQueue<Raw> establishJingleConnection(String toJID)
			throws XMPPException {
		if (seaJingle == null) {
			seaJingle = new SportEventAnalyserJingle(connection);
		}
		return seaJingle.establishConnection(toJID);
	}

	@Override
	public void disconnect() {
		if (connection != null && connection.isConnected()) {
			connection.disconnect();
			System.out.println("Disconnected");
		}
	}

	@Override
	protected void registerXMPPExtensions() {
		// TODO: register incoming beans
	}

}