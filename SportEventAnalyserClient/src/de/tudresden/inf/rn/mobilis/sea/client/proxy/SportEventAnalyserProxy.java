package de.tudresden.inf.rn.mobilis.sea.client.proxy;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
public class SportEventAnalyserProxy {

	private ISportEventAnalyserOutgoing _bindingStub;


	public SportEventAnalyserProxy( ISportEventAnalyserOutgoing bindingStub) {
		_bindingStub = bindingStub;
	}


	public ISportEventAnalyserOutgoing getBindingStub(){
		return _bindingStub;
	}


	public XMPPBean PlayerMappings( String toJid, IXMPPCallback< Mappings > callback ) {
		if ( null == _bindingStub || null == callback )
			return null;

		MappingRequest out = new MappingRequest(  );
		out.setTo( toJid );

		_bindingStub.sendXMPPBean( out, callback );

		return out;
	}

}