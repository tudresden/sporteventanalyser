package de.tudresden.inf.rn.mobilis.server.services.xslttest.service;

import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

import de.tudresden.inf.rn.mobilis.server.agents.MobilisAgent;
import de.tudresden.inf.rn.mobilis.server.services.MobilisService;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.listener.IQListener;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElement;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElementFull;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElementXS;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.InElementXSSequence;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.OutElementXS;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.XSLTTestProxy;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.impl.XSLTTestDispatcher;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.impl.XSLTTestDistributer;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.server.BeanProviderAdapter;

public class XSLTTestService extends MobilisService {

	public XSLTTestService() {
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
		// IQ-Listener
		getAgent().getConnection().addPacketListener(
				new IQListener(new XSLTTestDispatcher(new XSLTTestProxy(
						new XSLTTestDistributer(getAgent().getConnection())))),
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
		registerXMPPBean(new InElement());
		registerXMPPBean(new InElementFull());
		registerXMPPBean(new InElementXS());
		registerXMPPBean(new InElementXSSequence());
		
		// Fault
		registerXMPPBean(new OutElementXS());
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
