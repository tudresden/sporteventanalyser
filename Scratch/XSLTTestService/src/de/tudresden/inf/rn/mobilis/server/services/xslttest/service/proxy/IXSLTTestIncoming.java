package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public interface IXSLTTestIncoming {

	void onInOnlyOperation( InElement in );

	void onInOnlyOperationWithFault( InElementFull in );

	void onOutInOperationWithFault( InElementXS in );

	void onOutInOperationWithFaultError( OutElementXS in);

	XMPPBean onInOutOperationWithFault( InElementXSSequence in );

}