package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl;

import java.util.HashMap;
import java.util.Map;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PlayerStatistic;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.DataNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

public class CurrentPlayerData extends DataNode {

	private static final String NODENAME = "CurrentPlayerData";

	private Map<Integer, PlayerStatistic> playerStatistics;

	public CurrentPlayerData() {
		playerStatistics = new HashMap<Integer, PlayerStatistic>();
	}

	/**
	 * Registers a new <code>PlayerStatistic</code> to this
	 * <code>PlayerData</code>. All registered <code>PlayerStatistic</code>s are
	 * frequently published
	 * 
	 * @param playerStatistic
	 *            the <code>PlayerStatistic</code> which should be published
	 */
	public void registerPlayerStatistic(PlayerStatistic playerStatistic) {
		playerStatistics.put(playerStatistic.getId(), playerStatistic);
	}

	/**
	 * Get a registered <code>PlayerStatistic</code>
	 * 
	 * @param id
	 *            the ID of the <code>PlayerStatistic</code>
	 * @return the corresponding <code>PlayerStatistic</code> or
	 *         <code>null</code> if no <code>PlayerStatistic</code> exists with
	 *         such an ID
	 */
	public PlayerStatistic getPlayerStatistic(int id) {
		return playerStatistics.get(id);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String getNodeName() {
		return NODENAME;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<CurrentPlayerData>");

		// Append PlayerStatistic
		for (PlayerStatistic playerStatistic : playerStatistics.values()) {
			sb.append(playerStatistic.toXML());
		}

		sb.append("</CurrentPlayerData>");

		return sb.toString();
	}

	@Override
	public Node clone() {
		CurrentPlayerData clone = new CurrentPlayerData();
		for (PlayerStatistic playerStatistic : playerStatistics.values()) {
			clone.registerPlayerStatistic((PlayerStatistic) playerStatistic
					.clone());
		}
		return clone;
	}

}
