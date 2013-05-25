package de.tudresden.inf.rn.mobilis.xslttest.client.proxy.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.XMPPConnection;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.IXMPPCallback;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.IXSLTTestOutgoing;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.xmpp.BeanIQAdapter;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.xmpp.BeanProviderAdapter;

public abstract class XSLTTestDistributer implements IXSLTTestOutgoing {

	/**
	 * The <code>XMPPConnection</code> which is used to send/receive IQs
	 */
	protected XMPPConnection connection;

	/**
	 * The <code>HashMap</code> which is used to store the awaiting callbacks
	 */
	private ConcurrentHashMap<String, IXMPPCallback<? extends XMPPBean>> _awaitingCallbacks = new ConcurrentHashMap<String, IXMPPCallback<? extends XMPPBean>>();

	@Override
	public void sendXMPPBean(XMPPBean out,
			IXMPPCallback<? extends XMPPBean> callback) {
		// Put callback into _awaitingCallbacks
		_awaitingCallbacks.put(out.getId(), callback);

		// Send Bean
		sendXMPPBean(out);
	}

	@Override
	public void sendXMPPBean(XMPPBean out) {
		// Check if connected
		if (connection.isConnected()) {
			// Set From-JID
			out.setFrom(connection.getUser());

			// Finally send
			connection.sendPacket(new BeanIQAdapter(out));
		}
	}

	/**
	 * Register an XMPPBean as prototype
	 * 
	 * @param prototype
	 *            a basic instance of the XMPPBean
	 */
	protected void registerXMPPBean(XMPPBean prototype) {
		// add XMPPBean to service provider to enable it in XMPP
		(new BeanProviderAdapter(prototype)).addToProviderManager();
	}

	/**
	 * Connect to XMPP-Server
	 * 
	 * @param server
	 * @param port
	 * @param service
	 * @return
	 */
	public abstract boolean connect(String server, int port, String service);

	/**
	 * Try to login into XMPP-Server and update presence
	 * 
	 * @param username
	 * @param password
	 * @param ressource
	 * @param toJID
	 * @param distributer
	 * @return
	 */
	public abstract boolean performLogin(String username, String password,
			String ressource, String toJID);

	/**
	 * Disconnect from XMPP-Server
	 */
	public abstract void disconnect();

	/**
	 * Retrieves the <code>IXMPPCallback</code> which is linked to this
	 * packetID. This method will return null, if there does not exist an
	 * <code>IXMPPCallback</code> for this packetID.
	 * 
	 * @param packetID
	 *            the packetID whose <code>IXMPPCallback</code> should be
	 *            returned
	 * @return the registered <code>IXMPPCallback</code> for this packetID or
	 *         <code>null</code> if there is no <code>IXMPPCallback</code>
	 *         registered
	 */
	public IXMPPCallback<? extends XMPPBean> releaseAwaitingCallback(
			String packetID) {
		return _awaitingCallbacks.remove(packetID);
	}

	/**
	 * Register all XMBBBeans labeled as XMPP extensions
	 */
	protected abstract void registerXMPPExtensions();
}
