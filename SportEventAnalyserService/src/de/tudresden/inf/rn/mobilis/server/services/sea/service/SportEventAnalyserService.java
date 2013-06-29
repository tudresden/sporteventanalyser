package de.tudresden.inf.rn.mobilis.server.services.sea.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.core.Main;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer.ReceptionListener;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.InterruptionBegin;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.InterruptionEnd;
import de.tudresden.inf.rn.mobilis.sea.jingle.core.SportEventAnalyserJingle;
import de.tudresden.inf.rn.mobilis.sea.pubsub.core.SportEventAnalyserPubSub;
import de.tudresden.inf.rn.mobilis.server.agents.MobilisAgent;
import de.tudresden.inf.rn.mobilis.server.services.MobilisService;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.listener.IQListener;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.Mapping;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.MappingRequest;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.Mappings;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.SportEventAnalyserProxy;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.impl.SEADispatcher;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.impl.SEADistributer;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.server.BeanProviderAdapter;

public class SportEventAnalyserService extends MobilisService {

	private Main statistic;

	private Mappings mappings;
	private SportEventAnalyserPubSub seaPubSub;

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
		
		

		mappings = new Mappings();

		try {
			loadPlayerConfig();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	private void loadPlayerConfig() throws ParserConfigurationException,
			SAXException, IOException {
		SAXParserFactory.newInstance().newSAXParser()
				.parse(getClass().getResource("../../../../../../../../../META-INF/playerConfig.xml").getPath(), new DefaultHandler() {

					private Mapping mapping;

					private boolean xID, xName, xTeam;

					public void startElement(String uri, String localName,
							String qName, Attributes attributes)
							throws SAXException {
						if (qName.equals("player")) {
							mapping = new Mapping();
						} else if (qName.equals("id"))
							xID = true;	
						else if (qName.equals("name"))
							xName = true;
						else if (qName.equals("team"))
							xTeam = true;
					}

					public void endElement(String uri, String localName,
							String qName) throws SAXException {
						if (qName.equals("player"))
							mappings.getPlayerMappings().add(mapping);
					}

					public void characters(char ch[], int start, int length)
							throws SAXException {
						if (xID) {
							mapping.setPlayerID(Integer.valueOf(new String(ch, start, length)));
							xID = false;
						} else if (xName) {
							mapping.setPlayerName(new String(ch, start, length));
							xName = false;
						} else if (xTeam) {
							mapping.setTeamName(new String(ch, start, length));
							xTeam = false;
						}
					}
				});
	}

	@Override
	protected void registerPacketListener() {
		getAgent().getConnection().addPacketListener(
				new IQListener(new SEADispatcher(new SportEventAnalyserProxy(
						new SEADistributer(getAgent().getConnection())),
						mappings)), new PacketTypeFilter(IQ.class));

		
		// PubSub
				 seaPubSub = new SportEventAnalyserPubSub(getAgent().getConnection());
						
				// Initialize statistic
				statistic = new Main(seaPubSub.getStatistics());
		
		for (Mapping mapping : mappings.getPlayerMappings()) {
			//TODO: Remove Sysout
			System.out.println("Player: " + mapping.getPlayerName() + " (ID: " + mapping.getPlayerID() + ", Team: " + mapping.getTeamName() + ")");
			seaPubSub.getStatistics().registerPlayer(mapping.getPlayerID());
		}

		// Jingle
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
						statistic.sendEvent(event);
						// System.out.println(event.getSender() + ", "
						// + event.getTimestamp() + ", "
						// + event.getAcceleration());
						seaPubSub.getStatistics().setPositionOfPlayer(event.getSender(), event.getPositionX(), event.getPositionY(), event.getVelocityX(), event.getVelocityY());
						seaPubSub.getStatistics().setBallContacs(event.getSender(), statistic.getGameInformation().getPlayerBallContacts(event.getSender()));
//						c++;
//						if (c % 100000 == 0)
//							System.out.println("Received " + c + " Events in "
//									+ (System.currentTimeMillis() - cT) + "ms");
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
		registerXMPPBean(new MappingRequest());
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
