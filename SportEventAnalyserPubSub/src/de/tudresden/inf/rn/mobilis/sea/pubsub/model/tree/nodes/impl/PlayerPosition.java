package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.PositionNode;

public class PlayerPosition extends PositionNode<PlayerPosition> {

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
	public String toPredictiveCodedXML(PlayerPosition iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<PlayerPosition>");

		// ID
		sb.append("<id>");
		sb.append(id);
		sb.append("</id>");

		// Append super
		String s = super.toPredictiveCodedXML(iNode);
		if (s.length() > 0) {
			c = true;
			sb.append(s);
		}

		if (c) {
			sb.append("</PlayerPosition>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(PlayerPosition dest) {
		// Copy super
		super.copy(dest);
	}

	@Override
	public PlayerPosition clone() {
		return new PlayerPosition(this.getID(), this.getPositionX(),
				this.getPositionY(), this.getVelocityX(), this.getVelocityY());
	}

}
