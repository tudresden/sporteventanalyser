package de.tudresden.inf.rn.mobilis.sea.client.connection.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import de.tudresden.inf.rn.mobilis.sea.client.proxy.ISportEventAnalyserIncoming;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.xmpp.BeanIQAdapter;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class IQListener implements PacketListener {

	private ISportEventAnalyserIncoming dispatcher;

	public IQListener(ISportEventAnalyserIncoming dispatcher) {
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
			//TODO: distribute incoming beans
		}
	}

}
