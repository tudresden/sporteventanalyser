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
	 * This <code>Mappings</code>-<code>XMPPBean</code> holds all the concrete
	 * mappings for a game (PlayerID|Name|TeamName, GameFieldSize, Goals)
	 */
	private Mappings mappings;

	/**
	 * Constructor for a <code>SEADispatcher</code>
	 * 
	 * @param proxy
	 *            the <code>SportEventAnalyserProxy</code> to send
	 *            <code>XMPPBean</code>s
	 * @param mappings
	 *            a <code>XMPPBean</code> where at least all players are mapped
	 */
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
