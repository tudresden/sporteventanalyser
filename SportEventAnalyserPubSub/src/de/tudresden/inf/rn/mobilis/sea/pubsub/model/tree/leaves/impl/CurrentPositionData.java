package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.BallPosition;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PlayerPosition;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.DataNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * Concrete statistic <code>Node</code> which is used to set actual positions of
 * players
 */
public class CurrentPositionData extends DataNode {

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
	 * Removes a registered <code>PlayerPosition</code>. This may be useful when
	 * a player is substituted
	 * 
	 * @param id
	 *            the ID of the <code>PlayerPosition</code>
	 */
	public void removePlayerPosition(int id) {
		playerPositions.remove(id);
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
	public String getNodeName() {
		return NODENAME;
	}

	@Override
	public Node clone() {
		CurrentPositionData clone = new CurrentPositionData(
				(BallPosition) this.ballPosition.clone());
		// Append PlayerPositions
		for (PlayerPosition node : playerPositions.values()) {
			clone.registerPlayerPosition((PlayerPosition) node.clone());
		}

		return clone;
	}

}
