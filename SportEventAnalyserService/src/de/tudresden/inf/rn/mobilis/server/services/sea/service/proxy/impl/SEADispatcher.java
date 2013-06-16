package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.impl;

import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.ISportEventAnalyserIncoming;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.MappingRequest;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.Mappings;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.SportEventAnalyserProxy;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class SEADispatcher implements ISportEventAnalyserIncoming {

	private SportEventAnalyserProxy _proxy;

	private Mappings mappings;

//	private static int eventCounter = 0;
//	private static int messageCounter = 0;
//	private static long startPlayTime = 0;
//	private static long timeOfCycleStart = System.currentTimeMillis();

	public SEADispatcher(SportEventAnalyserProxy proxy, Mappings mappings) {
		this._proxy = proxy;
		this.mappings = mappings;
	}

//	@Override
//	public void onEventNotification(Events in) {
//
//		if (startPlayTime == 0)
//			startPlayTime = in.getSender().get(0).getTimestamp();
//
//		eventCounter += in.getSender().size();
//		messageCounter++;
//
//		if (System.currentTimeMillis() - timeOfCycleStart > 1000) {
//
//			long timeNeededInThisCycleInMS = System.currentTimeMillis()
//					- timeOfCycleStart;
//			int messagesPerSecond = (int) (messageCounter * 1000l / timeNeededInThisCycleInMS);
//			int eventsPerSecond = (int) (eventCounter * 1000l / timeNeededInThisCycleInMS);
//			long playtimeInMillisecs = (in.getSender().get(0).getTimestamp() - startPlayTime) / 1000 / 1000 / 1000;
//			int playtimeInMinutesOnly = (int) (playtimeInMillisecs / 1000 / 60);
//			int playtimeInSecondsOnly = (int) (playtimeInMillisecs / 1000 - (playtimeInMinutesOnly * 60));
//			System.out.println("INCOMING:  " + playtimeInMinutesOnly + "min "
//					+ playtimeInSecondsOnly + "s" + " playtime" + " | "
//					+ messagesPerSecond + " messages/s" + " | "
//					+ eventsPerSecond + " events/s");
//
//			timeOfCycleStart = System.currentTimeMillis();
//			eventCounter = 0;
//			messageCounter = 0;
//		}
//
//	}

	@Override
	public XMPPBean onPlayerMappings(MappingRequest in) {
		System.out.println("Mapping Request!");
		return null;
	}
}
