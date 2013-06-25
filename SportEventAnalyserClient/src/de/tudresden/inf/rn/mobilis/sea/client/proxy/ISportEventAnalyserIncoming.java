package de.tudresden.inf.rn.mobilis.sea.client.proxy;

public interface ISportEventAnalyserIncoming {

	void onGameMappings( Mappings in );

	void onGameMappingsError( MappingRequest in);

}