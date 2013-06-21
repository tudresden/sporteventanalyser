package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.impl;

import java.util.LinkedList;
import java.util.List;

import de.core.Config;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.GameField;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.Goal;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.ISportEventAnalyserIncoming;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.MappingRequest;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.Mappings;
import de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy.SportEventAnalyserProxy;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class SEADispatcher implements ISportEventAnalyserIncoming {

	private SportEventAnalyserProxy _proxy;

	private Mappings mappings;

	public SEADispatcher(SportEventAnalyserProxy proxy, Mappings mappings) {
		this._proxy = proxy;
		this.mappings = mappings;

		// Set size of gamefield
		mappings.setGameFieldSize(new GameField(Config.GAMEFIELDMINX,
				Config.GAMEFIELDMAXX, Config.GAMEFIELDMINY,
				Config.GAMEFIELDMAXY));

		// List of Goals
		List<Goal> goals = new LinkedList<Goal>();
		goals.add(new Goal(Config.GOALONEMINX, Config.GOALONEMAXX));
		goals.add(new Goal(Config.GOALTWOMINX, Config.GOALTWOMAXX));

		mappings.setGoals(goals);
	}

	@Override
	public XMPPBean onGameMappings(MappingRequest in) {
		_proxy.GameMappings(in.getFrom(), in.getId(),
				mappings.getGameFieldSize(), mappings.getGoals(),
				mappings.getPlayerMappings());
		return null;
	}

}
