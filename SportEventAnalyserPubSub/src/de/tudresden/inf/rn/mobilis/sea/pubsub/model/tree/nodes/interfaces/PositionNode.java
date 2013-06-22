package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces;

/**
 * This <code>PositionNode</code> is an auxiliary node to bundle the
 * x/y-position and x/y-velocity of a subject
 * 
 * @param <T>
 *            generic parameter to declare the type of this node
 */
public abstract class PositionNode<T extends PositionNode<T>> extends Node<T> {

	/**
	 * Position on the x-axis
	 */
	private int positionX;

	/**
	 * Position on the y-axis
	 */
	private int positionY;

	/**
	 * Velocity on the x-axis
	 */
	private int velocityX;

	/**
	 * Velocity on the y-axis
	 */
	private int velocityY;

	/**
	 * Constructor for a <code>PositionNode</code>
	 * 
	 * @param positionX
	 *            the position of the subject on the x-axis
	 * @param positionY
	 *            the position of the subject on the y-axis
	 * @param velocityX
	 *            the velocity of the subject on the x-axis
	 * @param velocityY
	 *            the velocity of the subject on the y-axis
	 */
	public PositionNode(int positionX, int positionY, int velocityX,
			int velocityY) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	/**
	 * Get the position on the x-axis
	 * 
	 * @return the positionX
	 */
	public int getPositionX() {
		return positionX;
	}

	/**
	 * Get the position on the y-axis
	 * 
	 * @return the positionY
	 */
	public int getPositionY() {
		return positionY;
	}

	/**
	 * Get the velocity on the x-axis
	 * 
	 * @return the velocityX
	 */
	public int getVelocityX() {
		return velocityX;
	}

	/**
	 * Get the velocity on the y-axis
	 * 
	 * @return the velocityY
	 */
	public int getVelocityY() {
		return velocityY;
	}

	/**
	 * Set the position on the x-axis
	 * 
	 * @param positionX
	 *            the positionX to set
	 */
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	/**
	 * Set the position on the y-axis
	 * 
	 * @param positionY
	 *            the positionY to set
	 */
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	/**
	 * Set the velocity on the x-axis
	 * 
	 * @param velocityX
	 *            the velocityX to set
	 */
	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}

	/**
	 * Set the velocity on the y-axis
	 * 
	 * @param velocityY
	 *            the velocityY to set
	 */
	public void setVelocityY(int velocityY) {
		this.velocityY = velocityY;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		// PositionX
		sb.append("<positionX>");
		sb.append(positionX);
		sb.append("</positionX>");

		// PositionY
		sb.append("<positionY>");
		sb.append(positionY);
		sb.append("</positionY>");

		// VelocityX
		sb.append("<velocityX>");
		sb.append(velocityX);
		sb.append("</velocityX>");

		// VelocityY
		sb.append("<velocityY>");
		sb.append(velocityY);
		sb.append("</velocityY>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(
			@SuppressWarnings("rawtypes") PositionNode iNode) {
		StringBuilder sb = new StringBuilder();

		// PositionX
		if (iNode.getPositionX() != this.getPositionX()) {
			sb.append("<positionX>");
			sb.append(this.getPositionX());
			sb.append("</positionX>");
		}
		// PositionY
		if (iNode.getPositionY() != this.getPositionY()) {
			sb.append("<positionY>");
			sb.append(this.getPositionY());
			sb.append("</positionY>");
		}
		// VelocityX
		if (iNode.getVelocityX() != this.getVelocityX()) {
			sb.append("<velocityX>");
			sb.append(this.getVelocityX());
			sb.append("</velocityX>");
		}
		// VelocityY
		if (iNode.getVelocityY() != this.getVelocityY()) {
			sb.append("<velocityY>");
			sb.append(this.getVelocityY());
			sb.append("</velocityY>");
		}

		return sb.toString();
	}

	@Override
	public void copy(@SuppressWarnings("rawtypes") PositionNode dest) {
		dest.setPositionX(positionX);
		dest.setPositionY(positionY);
		dest.setVelocityX(velocityX);
		dest.setVelocityY(velocityY);
	}

}
