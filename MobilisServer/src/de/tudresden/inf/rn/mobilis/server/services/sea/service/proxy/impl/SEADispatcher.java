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
		if (i % 10000 == 0)
			System.out.println(i + ", " + (System.currentTimeMillis() - cT));
	}

}
