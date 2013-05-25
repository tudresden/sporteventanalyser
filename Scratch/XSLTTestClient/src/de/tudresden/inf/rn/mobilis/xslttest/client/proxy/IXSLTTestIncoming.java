package de.tudresden.inf.rn.mobilis.xslttest.client.proxy;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public interface IXSLTTestIncoming {

	void onOutOnlyOperation( OutElement in );

	void onOutOnlyOperationWithFault( OutElementFull in );

	XMPPBean onOutInOperationWithFault( OutElementXS in );

	void onInOutOperationWithFault( OutElementXSSequence in );

	void onInOutOperationWithFaultError( InElementXSSequence in);

}