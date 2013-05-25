package de.tudresden.inf.rn.mobilis.server.services.sea.service;

import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

import de.tudresden.inf.rn.mobilis.server.agents.MobilisAgent;
import de.tudresden.inf.rn.mobilis.server.services.MobilisService;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.listener.IQListener;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.Events;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.SportEventAnalyserProxy;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.impl.SEADispatcher;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.impl.SEADistributer;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.server.BeanProviderAdapter;

public class SportEventAnalyserService extends MobilisService {

	public SportEventAnalyserService() {
		new Thread() {
			public void run() {
				Object o = new Object();
				synchronized (o) {
					try {
						o.wait();
					} catch (InterruptedException e) {
						// Ignore (just -nogui --keep-alive)
					}
				}
			}
		}.start();
	}

	@Override
	protected void registerPacketListener() {
		getAgent().getConnection().addPacketListener(
				new IQListener(new SEADispatcher(new SportEventAnalyserProxy(
						new SEADistributer(getAgent().getConnection())))),
				new PacketTypeFilter(IQ.class));
	}

	@Override
	public void startup(MobilisAgent agent) throws Exception {
		super.startup(agent);

		registerXMPPExtensions();
	}

	/**
	 * Register all XMBBBeans labeled as XMPP extensions
	 */
	public void registerXMPPExtensions() {
		// Events
		registerXMPPBean(new Events());
	}

	/**
	 * Register an XMPPBean as prototype
	 * 
	 * @param prototype
	 *            a basic instance of the XMPPBean
	 */
	private void registerXMPPBean(XMPPBean prototype) {
		// add XMPPBean to service provider to enable it in XMPP
		(new BeanProviderAdapter(prototype)).addToProviderManager();
	}
}
