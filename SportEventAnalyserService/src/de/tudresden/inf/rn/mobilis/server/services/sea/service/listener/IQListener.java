package de.tudresden.inf.rn.mobilis.server.services.sea.service.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.ISportEventAnalyserIncoming;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.MappingRequest;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.server.BeanIQAdapter;

/**
 * An <code>IQListener</code> implements the <code>PacketListener</code>
 * <code>interface</code>. It provides a mechanism to listen for packets that
 * pass a <code>PacketTypeFilter</code> to filter IQs. This allows event-style
 * programming -- every time a new packet is found, the
 * <code>processPacket(Packet)</code> method will be called
 */
public class IQListener implements PacketListener {

	/**
	 * A concrete dispatcher which must implement the
	 * <code>ISportEventAnalyserIncoming</code> <code>interface</code>
	 */
	private ISportEventAnalyserIncoming dispatcher;

	/**
	 * Constructor for an <code>IQListener</code>
	 * 
	 * @param dispatcher
	 *            a dispatcher which implements the
	 *            <code>ISportEventAnalyserIncoming</code>
	 *            <code>interface</code>
	 */
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
			if (xmppBean instanceof MappingRequest) {
				dispatcher.onGameMappings((MappingRequest) xmppBean);
			}
		}
	}
}
