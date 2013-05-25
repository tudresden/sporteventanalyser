package de.tudresden.inf.rn.mobilis.xslttest.client.connection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;

import de.tudresden.inf.rn.mobilis.xslttest.client.connection.listener.IQListener;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.InElement;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElement;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementFull;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementXS;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementXSSequence;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.XSLTTestProxy;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.impl.XSLTTestDispatcher;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.impl.XSLTTestDistributer;

public class MobilisConnectionManager extends XSLTTestDistributer {

	public MobilisConnectionManager() {
		// Default: false
		XMPPConnection.DEBUG_ENABLED = false;
	}

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
				connection.addPacketListener(new IQListener(
						new XSLTTestDispatcher(new XSLTTestProxy(this))),
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

	@Override
	public void disconnect() {
		if (connection != null && connection.isConnected()) {
			connection.disconnect();
			System.out.println("Disconnected");
		}
	}

	@Override
	protected void registerXMPPExtensions() {
		registerXMPPBean(new OutElement());
		registerXMPPBean(new InElement());
		registerXMPPBean(new OutElementFull());
		registerXMPPBean(new OutElementXS());
		registerXMPPBean(new OutElementXSSequence());
	}

}