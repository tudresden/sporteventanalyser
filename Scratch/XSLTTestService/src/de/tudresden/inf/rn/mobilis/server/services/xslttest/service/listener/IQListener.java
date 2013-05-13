package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.Event;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.IXSLTTestIncoming;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.server.BeanIQAdapter;

public class IQListener implements PacketListener {

	private IXSLTTestIncoming dispatcher;

	public IQListener(IXSLTTestIncoming dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void processPacket(Packet packet) {
		// Check if the incoming Packet is of type IQ (BeanIQAdapter is just a
		// wrapper)
		if (packet instanceof BeanIQAdapter) {
			// Convert packet to @see XMPPBean
			XMPPBean xmppBean = ((BeanIQAdapter) packet).getBean();

			// Distribute beans
			if (xmppBean instanceof //TODO) {
				dispatcher.onEventNotification((TODO) xmppBean);
			}
		}
	}
}
