package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.Connection;

import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.ISportEventAnalyserOutgoing;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.IXMPPCallback;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.server.BeanIQAdapter;

public class SEADistributer implements ISportEventAnalyserOutgoing {

	/**
	 * The <code>HashMap</code> which is used to store the awaiting callbacks
	 */
	private ConcurrentHashMap<String, IXMPPCallback<? extends XMPPBean>> _awaitingCallbacks = new ConcurrentHashMap<String, IXMPPCallback<? extends XMPPBean>>();

	/**
	 * The Smack-<code>Connection</code> which is used to send/receive IQs
	 */
	private Connection connection;

	/**
	 * Constructor for this <code>XSLTTestDistributer</code>
	 * 
	 * @param connection
	 *            the Smack-<code>Connection</code> which is used to
	 *            send/receive IQs
	 */
	public SEADistributer(Connection connection) {
		this.connection = connection;
	}

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
}
