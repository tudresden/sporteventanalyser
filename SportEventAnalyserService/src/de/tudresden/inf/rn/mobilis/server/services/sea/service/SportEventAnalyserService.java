package de.tudresden.inf.rn.mobilis.server.services.sea.service;

import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer.ReceptionListener;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.InterruptionBegin;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.InterruptionEnd;
import de.tudresden.inf.rn.mobilis.sea.jingle.core.SportEventAnalyserJingle;
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

		SportEventAnalyserJingle seaJingle = new SportEventAnalyserJingle(
				getAgent().getConnection());
		seaJingle.setReceptionListener(Event.PAYLOAD_TYPE,
				new ReceptionListener() {

					private boolean first = true;
					private long cT;
					private int c = 0;

					@Override
					public void handle(Raw item) {
						if (first) {
							cT = System.currentTimeMillis();
							first = false;
						}
						Event event = (Event) item;
						// System.out.println(event.getSender() + ", "
						// + event.getTimestamp() + ", "
						// + event.getAcceleration());
						c++;
						if (c % 100000 == 0)
							System.out.println("Received " + c + " Events in "
									+ (System.currentTimeMillis() - cT) + "ms");
					}

				});
		seaJingle.setReceptionListener(InterruptionBegin.PAYLOAD_TYPE,
				new ReceptionListener() {

					@Override
					public void handle(Raw item) {
						InterruptionBegin interruptionBegin = (InterruptionBegin) item;
						System.out.println("Interruption begins: "
								+ interruptionBegin.getBegin());
					}

				});
		seaJingle.setReceptionListener(InterruptionEnd.PAYLOAD_TYPE,
				new ReceptionListener() {

					@Override
					public void handle(Raw item) {
						InterruptionEnd interruptionEnd = (InterruptionEnd) item;
						System.out.println("Interruption ends: "
								+ interruptionEnd.getEnd());
					}

				});
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
