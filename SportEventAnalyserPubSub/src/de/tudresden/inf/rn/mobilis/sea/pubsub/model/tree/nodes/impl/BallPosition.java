package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.PositionNode;

public class BallPosition extends PositionNode<BallPosition> {

	/**
	 * Position on the z-axis
	 */
	private int positionZ;

	public BallPosition(int positionX, int positionY, int positionZ,
			int velocityX, int velocityY) {
		super(positionX, positionY, velocityX, velocityY);
		this.positionZ = positionZ;
	}

	/**
	 * Get the position on the z-axis
	 * 
	 * @return the positionZ
	 */
	public int getPositionZ() {
		return positionZ;
	}

	/**
	 * Set the position on the z-axis
	 * 
	 * @param positionZ
	 *            the positionZ to set
	 */
	public void setPositionZ(int positionZ) {
		this.positionZ = positionZ;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<BallPosition>");

		// PositionZ
		sb.append("<positionZ>");
		sb.append(positionZ);
		sb.append("</positionZ>");

		// Append super
		sb.append(super.toXML());

		sb.append("</BallPosition>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(BallPosition iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<BallPosition>");

		// PositionZ
		if (iNode.getPositionZ() != this.getPositionZ()) {
			c = true;
			sb.append("<positionZ>");
			sb.append(positionZ);
			sb.append("</positionZ>");
		}

		// Append super
		String s = super.toPredictiveCodedXML(iNode);
		if (s.length() > 0) {
			c = true;
			sb.append(s);
		}

		if (c) {
			sb.append("</BallPosition>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(BallPosition dest) {
		// Copy super
		super.copy(dest);

		dest.setPositionZ(positionZ);
	}

	@Override
	public BallPosition clone() {
		return new BallPosition(this.getPositionX(), this.getPositionY(),
				this.getPositionZ(), this.getVelocityX(), this.getVelocityY());
	}

}
