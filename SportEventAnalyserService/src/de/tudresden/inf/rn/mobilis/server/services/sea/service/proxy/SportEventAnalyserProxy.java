package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy;

import java.util.List;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
public class SportEventAnalyserProxy {

	private ISportEventAnalyserOutgoing _bindingStub;


	public SportEventAnalyserProxy( ISportEventAnalyserOutgoing bindingStub) {
		_bindingStub = bindingStub;
	}


	public ISportEventAnalyserOutgoing getBindingStub(){
		return _bindingStub;
	}


	public XMPPBean PlayerMappings( String toJid, String packetId, List< Mapping > Mappings ) {
		if ( null == _bindingStub )
			return null;

		Mappings out = new Mappings( Mappings );
		out.setTo( toJid );
		out.setId( packetId );

		_bindingStub.sendXMPPBean( out );

		return out;
	}

}