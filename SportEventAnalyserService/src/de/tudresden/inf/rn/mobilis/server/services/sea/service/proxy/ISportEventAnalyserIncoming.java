package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public interface ISportEventAnalyserIncoming {

	XMPPBean onPlayerMappings( MappingRequest in );

}