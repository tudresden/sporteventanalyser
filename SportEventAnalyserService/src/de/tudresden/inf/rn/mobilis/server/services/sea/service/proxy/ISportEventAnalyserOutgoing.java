package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
public interface ISportEventAnalyserOutgoing {

	void sendXMPPBean( XMPPBean out, IXMPPCallback< ? extends XMPPBean > callback );

	void sendXMPPBean( XMPPBean out );

}