package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy;

public class SportEventAnalyserProxy {

	private ISportEventAnalyserOutgoing _bindingStub;


	public SportEventAnalyserProxy( ISportEventAnalyserOutgoing bindingStub) {
		_bindingStub = bindingStub;
	}


	public ISportEventAnalyserOutgoing getBindingStub(){
		return _bindingStub;
	}


}