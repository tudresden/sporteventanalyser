package de.tudresden.inf.rn.mobilis.sea.client.proxy;

import java.util.List;import java.util.ArrayList;public class SportEventAnalyserProxy {

	private ISportEventAnalyserOutgoing _bindingStub;


	public SportEventAnalyserProxy( ISportEventAnalyserOutgoing bindingStub) {
		_bindingStub = bindingStub;
	}


	public ISportEventAnalyserOutgoing getBindingStub(){
		return _bindingStub;
	}


	public void EventNotification( String toJid, List< Event > Sender ) {
		if ( null == _bindingStub )
			return;

		Events out = new Events( Sender );
		out.setTo( toJid );

		_bindingStub.sendXMPPBean( out );
	}

}