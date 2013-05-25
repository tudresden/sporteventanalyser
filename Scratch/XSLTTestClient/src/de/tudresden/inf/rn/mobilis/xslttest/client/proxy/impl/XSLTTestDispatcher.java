package de.tudresden.inf.rn.mobilis.xslttest.client.proxy.impl;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.IXMPPCallback;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.IXSLTTestIncoming;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.InElementXSSequence;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElement;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementFull;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementXS;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementXSSequence;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.XSLTTestProxy;

public class XSLTTestDispatcher implements IXSLTTestIncoming {

	public XSLTTestProxy _proxy;

	public XSLTTestDispatcher(XSLTTestProxy proxy) {
		_proxy = proxy;
	}

	@Override
	public void onOutOnlyOperationWithFault(OutElementFull in) {
		System.out.println(in.toXML());
	}

	@Override
	public XMPPBean onOutInOperationWithFault(OutElementXS in) {
		//TODO
		return null;
	}

	@Override
	public void onOutOnlyOperation(OutElement in) {
		System.out.println(in.toXML());
	}

	@Override
	public void onInOutOperationWithFault(OutElementXSSequence in) {
		System.out.println(in.toXML());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onInOutOperationWithFaultError(InElementXSSequence in) {
		IXMPPCallback<? extends XMPPBean> _callback = ((XSLTTestDistributer) _proxy
				.getBindingStub()).releaseAwaitingCallback(in.getId());
		if (_callback == null) {
			// Something weird happened.... This method should only be used with
			// an callback!
		} else {
			((IXMPPCallback<InElementXSSequence>) _callback).invoke(in);
		}
	}

}
