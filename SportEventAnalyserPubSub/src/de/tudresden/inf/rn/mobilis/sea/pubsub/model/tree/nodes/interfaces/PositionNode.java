package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces;

public abstract class PositionNode extends Node {

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

}
