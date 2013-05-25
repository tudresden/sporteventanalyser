package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.IXSLTTestIncoming;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElement;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElementFull;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElementXS;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElementXSSequence;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.OutElementXS;
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
			if (xmppBean instanceof InElement) {
				dispatcher.onInOnlyOperation((InElement) xmppBean);
			} else if (xmppBean instanceof InElementFull) {
				dispatcher.onInOnlyOperationWithFault((InElementFull) xmppBean);
			} else if (xmppBean instanceof InElementXS) {
				dispatcher.onOutInOperationWithFault((InElementXS) xmppBean);
			} else if (xmppBean instanceof InElementXSSequence) {
				dispatcher
						.onInOutOperationWithFault((InElementXSSequence) xmppBean);
			} else if (xmppBean instanceof OutElementXS) {
				dispatcher.onOutInOperationWithFaultError((OutElementXS) xmppBean);
			}
		}
	}
}
