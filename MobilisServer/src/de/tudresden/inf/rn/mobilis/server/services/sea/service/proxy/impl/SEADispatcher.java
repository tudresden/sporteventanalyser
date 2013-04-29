package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.impl;

import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.Event;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.ISportEventAnalyserIncoming;

public class SEADispatcher implements ISportEventAnalyserIncoming {

	private long cT;

	private int i;

	private boolean bench;

	public SEADispatcher() {
		bench = true;
	}

	@Override
	public void onEventNotification(Event in) {
		if (bench) {
			bench = false;
			cT = System.currentTimeMillis();
			i = 0;
		}
		i++;
		long t = System.currentTimeMillis() - cT;
		if (i % 10000 == 0)
//		if (t > 60000 && t < 61000)
			System.out.println(i + ", " + t);
	}

}
