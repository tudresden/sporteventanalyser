package de.tudresden.inf.rn.mobilis.sea.client.proxy.impl;

import de.tudresden.inf.rn.mobilis.sea.client.proxy.ISportEventAnalyserIncoming;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.SportEventAnalyserProxy;


public class SEADispatcher implements ISportEventAnalyserIncoming {

	private SportEventAnalyserProxy _proxy;
	
	public SEADispatcher(SportEventAnalyserProxy proxy) {
		this._proxy = proxy;
	}

}
