package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.BallPosition;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PlayerPosition;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.DataNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * Concrete statistic <code>Node</code> which is used to set actual positions of
 * players
 */
public class CurrentPositionData extends DataNode<CurrentPositionData> {

	private static final String NODENAME = "CurrentPositionData";

	private BallPosition ballPosition;

	private Map<Integer, PlayerPosition> playerPositions;

	public CurrentPositionData() {
		ballPosition = new BallPosition(0, 0, 0, 0, 0);
		playerPositions = new HashMap<Integer, PlayerPosition>();
	}

	/**
	 * Private <code>CurrentPositionData</code> constructor (for cloning
	 * purposes)
	 * 
	 * @param ballPosition
	 *            the original <code>BallPosition</code>
	 */
	private CurrentPositionData(BallPosition ballPosition) {
		this.ballPosition = ballPosition;
		playerPositions = new HashMap<Integer, PlayerPosition>();
	}

	/**
	 * Registers a new <code>PlayerPosition</code> to this
	 * <code>CurrentPositionData</code>. All registered
	 * <code>PlayerPosition</code>s are frequently published
	 * 
	 * @param playerPosition
	 *            the <code>PlayerPosition</code> which should be published
	 */
	public void registerPlayerPosition(PlayerPosition playerPosition) {
		playerPositions.put(playerPosition.getID(), playerPosition);
	}

	/**
	 * Get all registered <code>PlayerPosition</code>s
	 * 
	 * @return all <code>PlayerPosition</code> as a <code>Collection</code>
	 */
	public Collection<PlayerPosition> getPlayerPositions() {
		return playerPositions.values();
	}

	/**
	 * Get a registered <code>PlayerPosition</code>
	 * 
	 * @param id
	 *            the ID of the <code>PlayerPosition</code>
	 * @return the corresponding <code>PlayerPosition</code> or
	 *         <code>null</code> if no <code>PlayerPosition</code> exists with
	 *         such an ID
	 */
	public PlayerPosition getPlayerPosition(int id) {
		return playerPositions.get(id);
	}

	/**
	 * Get the <code>BallPosition</code>
	 * 
	 * @return the <code>BallPosition</code>
	 */
	public BallPosition getBallPosition() {
		return ballPosition;
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

		sb.append("<CurrentPositionData>");

		// Append BallPosition
		sb.append(ballPosition.toXML());

		// Append PlayerPosition
		for (PlayerPosition node : playerPositions.values()) {
			sb.append(node.toXML());
		}

		sb.append("</CurrentPositionData>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(CurrentPositionData iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<CurrentPositionData>");

		// Append BallPosition
		String s = ballPosition.toPredictiveCodedXML(iNode.getBallPosition());
		if (s.length() > 0) {
			c = true;
			sb.append(s);
		}

		// Append PlayerPosition
		for (PlayerPosition node : playerPositions.values()) {
			if (iNode.getPlayerPosition(node.getID()) == null) {
				s = node.toXML();
			} else {
				s = node.toPredictiveCodedXML(iNode.getPlayerPosition(node
						.getID()));
			}
			if (s.length() > 0) {
				c = true;
				sb.append(s);
			}
		}

		if (c) {
			sb.append("</CurrentPositionData>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(CurrentPositionData dest) {
		// Copy BallPosition
		ballPosition.copy(dest.getBallPosition());

		// Copy PlayerPositons
		PlayerPosition destPlayerPosition;
		for (PlayerPosition playerPosition : playerPositions.values()) {
			if ((destPlayerPosition = dest.getPlayerPosition(playerPosition
					.getID())) == null) {
				dest.registerPlayerPosition(playerPosition.clone());
			} else {
				playerPosition.copy(destPlayerPosition);
			}
		}
	}

	@Override
	public CurrentPositionData clone() {
		CurrentPositionData clone = new CurrentPositionData(
				this.ballPosition.clone());
		// Append PlayerPositions
		for (PlayerPosition node : playerPositions.values()) {
			clone.registerPlayerPosition(node.clone());
		}

		return clone;
	}

}
