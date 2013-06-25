package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl;

import java.util.HashMap;
import java.util.Map;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PlayerStatistic;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.DataNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * This <code>CurrentPlayerData</code> is a concrete <code>DataNode</code>. It
 * holds all the data of a player
 */
public class CurrentPlayerData extends DataNode<CurrentPlayerData> {

	/**
	 * The name of this node
	 */
	private static final String NODENAME = "CurrentPlayerData";

	/**
	 * A <code>Map</code> which holds all known <code>PlayerStatistic</code>s
	 * (key: ID of the player, value: concrete <code>PlayerStatistic</code>)
	 */
	private Map<Integer, PlayerStatistic> playerStatistics;

	/**
	 * Constructor for this <code>CurrentPlayerData</code>
	 */
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
	public String toPredictiveCodedXML(CurrentPlayerData iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<CurrentPlayerData>");

		// Append PlayerStatistic
		String s;
		for (PlayerStatistic playerStatistic : playerStatistics.values()) {
			if (iNode.getPlayerStatistic(playerStatistic.getId()) == null) {
				s = playerStatistic.toXML();
			} else {
				s = playerStatistic.toPredictiveCodedXML(iNode
						.getPlayerStatistic(playerStatistic.getId()));
			}
			if (s.length() > 0) {
				c = true;
				sb.append(s);
			}
		}

		if (c) {
			sb.append("</CurrentPlayerData>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(CurrentPlayerData dest) {
		// Copy PlayerStatistics
		PlayerStatistic destPlayerStatistic;
		for (PlayerStatistic playerStatistic : playerStatistics.values()) {
			if ((destPlayerStatistic = dest.getPlayerStatistic(playerStatistic
					.getId())) == null) {
				dest.registerPlayerStatistic(playerStatistic.clone());
			} else {
				playerStatistic.copy(destPlayerStatistic);
			}
		}
	}

	@Override
	public CurrentPlayerData clone() {
		CurrentPlayerData clone = new CurrentPlayerData();
		for (PlayerStatistic playerStatistic : playerStatistics.values()) {
			clone.registerPlayerStatistic(playerStatistic.clone());
		}
		return clone;
	}

}
