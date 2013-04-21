package de.tudresden.inf.rn.mobilis.sea.client.proxy.impl;

import de.tudresden.inf.rn.mobilis.sea.client.proxy.ISportEventAnalyserOutgoing;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.IXMPPCallback;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public abstract class SEADistributer implements ISportEventAnalyserOutgoing {

	@Override
	public void sendXMPPBean(XMPPBean out,
			IXMPPCallback<? extends XMPPBean> callback) {
		// TODO: Handle callbacks?! If we need them...
	}

	@Override
	public void sendXMPPBean(XMPPBean out) {
		// Append some tags (from, type)
		out.setFrom(this.getFullFromJID());
		out.setType(XMPPBean.TYPE_SET);

		// Try to send XMPPBean as IQ
		this.sendBean(out);
	}

	protected abstract void sendBean(XMPPBean out);

	/**
	 * Returns the full From-JID
	 * 
	 * @return the From-JID
	 */
	public abstract String getFullFromJID();
}
