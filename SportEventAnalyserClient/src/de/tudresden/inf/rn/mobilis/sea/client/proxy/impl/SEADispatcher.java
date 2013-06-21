package de.tudresden.inf.rn.mobilis.sea.client.proxy.impl;

import de.tudresden.inf.rn.mobilis.sea.client.proxy.ISportEventAnalyserIncoming;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.MappingRequest;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.Mappings;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.SportEventAnalyserProxy;


public class SEADispatcher implements ISportEventAnalyserIncoming {

	private SportEventAnalyserProxy _proxy;
	
	public SEADispatcher(SportEventAnalyserProxy proxy) {
		this._proxy = proxy;
	}

	@Override
	public void onGameMappings(Mappings in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameMappingsError(MappingRequest in) {
		// TODO Auto-generated method stub
		
	}

}
