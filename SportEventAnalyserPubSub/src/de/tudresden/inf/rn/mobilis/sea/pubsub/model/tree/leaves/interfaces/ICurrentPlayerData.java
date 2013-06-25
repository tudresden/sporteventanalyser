package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.interfaces;

/**
 * This is an auxiliary <code>interface</code> to declare some methods which may
 * be done on a <code>CurrentPlayerData</code> node
 */
public interface ICurrentPlayerData {

	/**
	 * Set the statistic values of a player. The player must be registered first
	 * ( <code>StatisticsFacade.registerPlayer(id)</code>). Otherwise nothing
	 * will be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param passesMade
	 *            the number of passes made by this player
	 * @param passesReceived
	 *            the number of passes received by this player
	 * @param tacklings
	 *            the number of tacklings of this player
	 * @param tacklesWon
	 *            the number of successful tacklings of this player
	 * @param goalsScored
	 *            the number of goals scored by this player
	 * @param ballContacts
	 *            the number of ball contacts of this player
	 * @param possessionTime
	 *            the total amount of time on which this player had possession
	 *            of the ball
	 */
	public void setPlayerStatistic(int id, int passesMade, int passesReceived,
			int tacklings, int tacklesWon, int goalsScored, int ballContacts,
			long possessionTime);

	/**
	 * Set the number of passes made by this player. The player must be
	 * registered first ( <code>StatisticsFacade.registerPlayer(id)</code>).
	 * Otherwise nothing will be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param passesMade
	 *            the number of passes made by this player
	 */
	public void setPassesMade(int id, int passesMade);

	/**
	 * Set the number of passes received by this player. The player must be
	 * registered first ( <code>StatisticsFacade.registerPlayer(id)</code>).
	 * Otherwise nothing will be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param passesReceived
	 *            the number of passes received by this player
	 */
	public void setPassesReceived(int id, int passesReceived);

	/**
	 * Set the number of tacklings of this player. The player must be registered
	 * first ( <code>StatisticsFacade.registerPlayer(id)</code>). Otherwise
	 * nothing will be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param tacklings
	 *            the number of tacklings of this player
	 */
	public void setTacklings(int id, int tacklings);

	/**
	 * Set the number of successful tacklings of this player. The player must be
	 * registered first ( <code>StatisticsFacade.registerPlayer(id)</code>).
	 * Otherwise nothing will be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param tacklesWon
	 *            the number of successful tacklings of this player
	 */
	public void setTacklesWon(int id, int tacklesWon);

	/**
	 * Set the number of goals scored by this player. The player must be
	 * registered first ( <code>StatisticsFacade.registerPlayer(id)</code>).
	 * Otherwise nothing will be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param goalsScored
	 *            the number of goals scored by this player
	 */
	public void setGoalsScored(int id, int goalsScored);

	/**
	 * Set the number of ball contacts of this player. The player must be
	 * registered first ( <code>StatisticsFacade.registerPlayer(id)</code>).
	 * Otherwise nothing will be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param ballContacts
	 *            the number of ball contacts of this player
	 */
	public void setBallContacs(int id, int ballContacts);

	/**
	 * Set the total amount of time on which this player had possession of the
	 * ball. The player must be registered first (
	 * <code>StatisticsFacade.registerPlayer(id)</code>). Otherwise nothing will
	 * be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param possessionTime
	 *            the total amount of time on which this player had possession
	 *            of the ball
	 */
	public void setPossessionTime(int id, long possessionTime);

}
