package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.impl;

import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.Events;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.ISportEventAnalyserIncoming;

public class SEADispatcher implements ISportEventAnalyserIncoming {

	private static final int OUTPUT_FREQUENCY_IN_MESSAGES_PER_CYCLE = 2000;
	private static int eventCounter = 0;
	private static int messageCounter = 0;
	private static long firstTimeStamp = 0;
	private static long timeOfCycleStart = 0;

	public SEADispatcher() {
	}

	@Override
	public void onEventNotification(Events in) {

		if (firstTimeStamp == 0)
			firstTimeStamp = in.getSender().get(0).getTimestamp();

		eventCounter += in.getSender().size();
		messageCounter++;

		if (messageCounter > OUTPUT_FREQUENCY_IN_MESSAGES_PER_CYCLE) {

			long timeNeededInThisCycleInMS = System.currentTimeMillis()
					- timeOfCycleStart;

			int messagesPerSecond = (int) (OUTPUT_FREQUENCY_IN_MESSAGES_PER_CYCLE * 1000l / timeNeededInThisCycleInMS);
			int eventsPerSecond = (int) (eventCounter * 1000l / timeNeededInThisCycleInMS);
			long playtimeInMillisecs = (in.getSender().get(0).getTimestamp() - firstTimeStamp) / 1000 / 1000 / 1000;
			int playtimeInMinutesOnly = (int) (playtimeInMillisecs / 1000 / 60);
			int playtimeInSecondsOnly = (int) (playtimeInMillisecs / 1000 - (playtimeInMinutesOnly * 60));
			System.out.println("INCOMING:  " + playtimeInMinutesOnly + "min "
					+ playtimeInSecondsOnly + "s" + " playtime" + " | "
					+ messagesPerSecond + " messages/s" + " | "
					+ eventsPerSecond + " events/s");

			timeOfCycleStart = System.currentTimeMillis();
			eventCounter = 0;
			messageCounter = 0;
		}

	}
}
