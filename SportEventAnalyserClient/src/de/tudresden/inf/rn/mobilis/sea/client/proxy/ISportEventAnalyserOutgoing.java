package de.tudresden.inf.rn.mobilis.sea.client.proxy;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public interface ISportEventAnalyserOutgoing {

	void sendXMPPBean( XMPPBean out, IXMPPCallback< ? extends XMPPBean > callback );

	void sendXMPPBean( XMPPBean out );

}