package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.interfaces;

public interface ICurrentPositionData {

	/**
	 * Sets the position of a player. The player must be registered first (
	 * <code>StatisticsFacade.registerPlayer(id)</code>). Otherwise nothing will
	 * be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param positionX
	 *            the position on the x-axis
	 * @param positionY
	 *            the position on the y-axis
	 * @param velocityX
	 *            the velocity on the x-axis
	 * @param velocityY
	 *            the velocity on the y-axis
	 */
	public void setPositionOfPlayer(int id, int positionX, int positionY,
			int velocityX, int velocityY);

	/**
	 * Sets the current position of the ball
	 * 
	 * @param positionX
	 *            the position on the x-axis
	 * @param positionY
	 *            the position on the y-axis
	 * @param positionZ
	 *            the position on the z-axis
	 * @param velocityX
	 *            the velocity on the x-axis
	 * @param velocityY
	 *            the velocity on the y-axis
	 */
	public void setPositionOfBall(int positionX, int positionY, int positionZ,
			int velocityX, int velocityY);
}
