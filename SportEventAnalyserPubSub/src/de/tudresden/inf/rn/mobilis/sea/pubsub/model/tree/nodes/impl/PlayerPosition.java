package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.PositionNode;

public class PlayerPosition extends PositionNode {

	/**
	 * ID of this player
	 */
	private int id;

	public PlayerPosition(int id, int positionX, int positionY, int velocityX,
			int velocityY) {
		super(positionX, positionY, velocityX, velocityY);
		this.id = id;
	}

	/**
	 * Get the ID of this player
	 * 
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<PlayerPosition>");

		// ID
		sb.append("<id>");
		sb.append(id);
		sb.append("</id>");

		// Append super
		sb.append(super.toXML());

		sb.append("</PlayerPosition>");

		return sb.toString();
	}

	@Override
	public Node clone() {
		return new PlayerPosition(this.getID(), this.getPositionX(),
				this.getPositionY(), this.getVelocityX(), this.getVelocityY());
	}

}
