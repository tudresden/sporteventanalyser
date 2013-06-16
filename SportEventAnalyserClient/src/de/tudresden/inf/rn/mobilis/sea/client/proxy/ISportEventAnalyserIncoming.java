package de.tudresden.inf.rn.mobilis.sea.client.proxy;

public interface ISportEventAnalyserIncoming {

	void onPlayerMappings( Mappings in );

	void onPlayerMappingsError( MappingRequest in);

}