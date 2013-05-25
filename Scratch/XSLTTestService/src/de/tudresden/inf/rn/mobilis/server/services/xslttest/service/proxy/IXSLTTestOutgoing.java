package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
public interface IXSLTTestOutgoing {

	void sendXMPPBean( XMPPBean out, IXMPPCallback< ? extends XMPPBean > callback );

	void sendXMPPBean( XMPPBean out );

}