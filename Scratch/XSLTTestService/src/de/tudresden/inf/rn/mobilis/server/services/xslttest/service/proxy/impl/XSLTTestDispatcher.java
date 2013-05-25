package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.impl;

import java.util.LinkedList;
import java.util.List;

import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.IXMPPCallback;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.IXSLTTestIncoming;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InComplexXSSequence;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElement;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElementFull;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElementXS;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElementXSSequence;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.OutComplexXSSequence;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.OutElementXS;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.OutElementXSSequence;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.XSLTTestProxy;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class XSLTTestDispatcher implements IXSLTTestIncoming {

	private XSLTTestProxy _proxy;

	public XSLTTestDispatcher(XSLTTestProxy proxy) {
		_proxy = proxy;
	}

	@Override
	public void onInOnlyOperationWithFault(InElementFull in) {
		System.out.println(in.toXML());
		// Just response
		List<InComplexXSSequence> inSeq = in.getInComplexSeq();
		List<OutComplexXSSequence> outSeq = new LinkedList<OutComplexXSSequence>();
		for (InComplexXSSequence inComplex : inSeq) {
			outSeq.add(new OutComplexXSSequence(inComplex.getInSeq()));
		}
		_proxy.OutOnlyOperationWithFault(in.getFrom(), in.getIntVal(),
				in.getLongVal(), in.getBooleanVal(), in.getDoubleVal(),
				in.getFloatVal(), in.getByteVal(), in.getShortVal(),
				in.getStringVal(), outSeq);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onOutInOperationWithFault(InElementXS in) {
		IXMPPCallback<? extends XMPPBean> _callback = ((XSLTTestDistributer) _proxy
				.getBindingStub()).releaseAwaitingCallback(in.getId());
		if (_callback == null) {
			// Something weird happened.... This method should only be used with
			// an callback!
		} else {
			((IXMPPCallback<InElementXS>) _callback).invoke(in);
		}
	}

	@Override
	public void onOutInOperationWithFaultError(OutElementXS in) {
		System.out.println(in.toXML());
	}

	@Override
	public void onInOnlyOperation(InElement in) {
		System.out.println(in.toXML());
		// Just response
		_proxy.OutOnlyOperation(in.getFrom());
		
		// Another OutInOperation
		_proxy.OutInOperationWithFault(in.getFrom(), 213l, new IXMPPCallback<InElementXS>() {

			@Override
			public void invoke(InElementXS xmppBean) {
				System.out.println(xmppBean.toXML());
			}
			
		});
	}

	@Override
	public XMPPBean onInOutOperationWithFault(InElementXSSequence in) {
		System.out.println(in.toXML());
		_proxy.getBindingStub().sendXMPPBean(in.buildOutFaultSimple("Detailed errored text message!"));
		return new OutElementXSSequence(in.getInSeq());
	}

}
