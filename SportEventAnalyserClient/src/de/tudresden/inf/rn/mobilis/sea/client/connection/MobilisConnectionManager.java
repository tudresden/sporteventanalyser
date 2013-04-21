package de.tudresden.inf.rn.mobilis.sea.client.connection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import de.tudresden.inf.rn.mobilis.sea.client.proxy.impl.SEADistributer;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.xmpp.BeanIQAdapter;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class MobilisConnectionManager extends SEADistributer {

	/**
	 * Container-class
	 */
	private class ConnectionConfig {
		private String username;
		private String ressource;
		private String service;

		public ConnectionConfig(String service) {
			this.service = service;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getRessource() {
			return ressource;
		}

		public void setRessource(String ressource) {
			this.ressource = ressource;
		}

		public String getService() {
			return service;
		}
	}

	private XMPPConnection connection;

	private ConnectionConfiguration config;

	private ConnectionConfig connectionConfig;

	public MobilisConnectionManager() {
		// Default: false
		XMPPConnection.DEBUG_ENABLED = false;
	}

	public MobilisConnectionManager(boolean debugEnabled) {
		XMPPConnection.DEBUG_ENABLED = debugEnabled;
	}

	/**
	 * Connect to XMPP-Server
	 * 
	 * @param server
	 * @param port
	 * @param service
	 * @return
	 */
	public boolean connect(String server, int port, String service) {
		connectionConfig = new ConnectionConfig(service);
		if (service == null || service.equals(""))
			config = new ConnectionConfiguration(server, port);
		else
			config = new ConnectionConfiguration(server, port, service);
		connection = new XMPPConnection(config);
		try {
			connection.connect();
		} catch (XMPPException e) {
			System.out.println("Error. Could not connect to XMPP-Server");
			return false;
		}

		System.out.println("Connected to: " + connection.getHost());
		return true;
	}

	/**
	 * Try to login into XMPP-Server and send update presence
	 * 
	 * @param username
	 * @param password
	 * @param ressource
	 * @param toJID
	 * @param distributer
	 * @return
	 */
	public boolean performLogin(String username, String password,
			String ressource, String toJID) {
		if (connection != null && connection.isConnected()) {
			connectionConfig.setUsername(username);
			connectionConfig.setRessource(ressource);
			try {
				connection.login(username, password, ressource);
			} catch (XMPPException e) {
				System.out.println("Error. Could not login to XMPP-Server!");
				return false;
			}

			// Send presence:
			Presence presence = new Presence(Presence.Type.available);
			presence.setTo(toJID);
			presence.setFrom(getFullFromJID());
			connection.sendPacket(presence);

			System.out.println("Logged in on server " + connection.getHost()
					+ " as user: " + username);
			return true;
		}
		return false;
	}

	/**
	 * Disconnect from XMPP-Server
	 */
	public void disconnect() {
		if (connection != null && connection.isConnected()) {
			connection.disconnect();
		}
	}

	@Override
	public String getFullFromJID() {
		return connectionConfig.getUsername() + "@"
				+ connectionConfig.getService() + "/"
				+ connectionConfig.getRessource();
	}

	@Override
	protected void sendBean(XMPPBean out) {
		// Check if connected
		if (connection.isConnected()) {
			// Finally send
			connection.sendPacket(new BeanIQAdapter(out));
		}
	}

}