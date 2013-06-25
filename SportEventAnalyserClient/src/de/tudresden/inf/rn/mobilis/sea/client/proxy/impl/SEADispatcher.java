package de.tudresden.inf.rn.mobilis.sea.client.proxy.impl;

import de.tudresden.inf.rn.mobilis.sea.client.proxy.ISportEventAnalyserIncoming;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.MappingRequest;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.Mappings;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.SportEventAnalyserProxy;

/**
 * This <code>SEADispatcher</code> implements the
 * <code>ISportEventAnalyserIncoming</code> <code>interface</code> and
 * dispatches all incoming requests
 */
public class SEADispatcher implements ISportEventAnalyserIncoming {

	/**
	 * This <code>SportEventAnalyserProxy</code> is used to send
	 * <code>XMPPBean</code>s
	 */
	private SportEventAnalyserProxy _proxy;

	/**
	 * Constructor for a <code>SEADispatcher</code>
	 * 
	 * @param proxy
	 *            the <code>SportEventAnalyserProxy</code> to send
	 *            <code>XMPPBean</code>s
	 */
	public SEADispatcher(SportEventAnalyserProxy proxy) {
		this._proxy = proxy;
	}

	@Override
	public void onGameMappings(Mappings in) {
		// NOOP (Java-Clients should never receive an IQ)
	}

	@Override
	public void onGameMappingsError(MappingRequest in) {
		// NOOP (Java-Clients should never receive an IQ)
	}

}
