package de.tudresden.inf.rn.mobilis.xslttest.client.connection.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.IXSLTTestIncoming;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElement;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementFull;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementXS;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementXSSequence;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.xmpp.BeanIQAdapter;

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
			if (xmppBean instanceof OutElement) {
				dispatcher.onOutOnlyOperation((OutElement) xmppBean);
			} else if (xmppBean instanceof OutElementFull) {
				dispatcher
						.onOutOnlyOperationWithFault((OutElementFull) xmppBean);
			} else if (xmppBean instanceof OutElementXS) {
				dispatcher.onOutInOperationWithFault((OutElementXS) xmppBean);
			} else if (xmppBean instanceof OutElementXSSequence) {
				dispatcher
						.onInOutOperationWithFault((OutElementXSSequence) xmppBean);
			}
		}
	}

}
