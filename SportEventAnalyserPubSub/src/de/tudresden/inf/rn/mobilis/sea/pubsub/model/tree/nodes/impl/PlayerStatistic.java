package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;

/**
 * A <code>PlayerStatistic</code> may be used to set all statistical parameters
 * of a player
 */
public class PlayerStatistic extends Node<PlayerStatistic> {

	/**
	 * ID of this player
	 */
	private int id;

	/**
	 * Number of passes made by this player
	 */
	private int passesMade;

	/**
	 * Number of passes missed by this player
	 */
	private int passesMissed;

	/**
	 * Number of passes received by this player
	 */
	private int passesReceived;

	/**
	 * Number of tacklings of this player
	 */
	private int tacklings;

	/**
	 * Number of successful tacklings of this player
	 */
	private int tacklesWon;

	/**
	 * Number of goals scored by this player
	 */
	private int goalsScored;

	/**
	 * Number of ball contacts of this player
	 */
	private int ballContacts;

	/**
	 * Total amount of time on which this player had possession of the ball
	 */
	private long possessionTime;

	/**
	 * Constructor for a <code>PlayerStatistic</code>
	 * 
	 * @param id
	 *            ID of this player
	 * @param passesMade
	 *            number of passes made by this player
	 * @param passesReceived
	 *            number of passes received by this player
	 * @param tacklings
	 *            number of tacklings of this player
	 * @param tacklesWon
	 *            number of successful tacklings of this player
	 * @param goalsScored
	 *            number of goals scored by this player
	 * @param ballContacts
	 *            number of ball contacts of this player
	 * @param possessionTime
	 *            total amount of time on which this player had possession of
	 *            the ball
	 */
	public PlayerStatistic(int id, int passesMade, int passesReceived,
			int tacklings, int tacklesWon, int goalsScored, int ballContacts,
			long possessionTime) {
		this.id = id;
		this.passesMade = passesMade;
		this.passesReceived = passesReceived;
		this.tacklings = tacklings;
		this.tacklesWon = tacklesWon;
		this.goalsScored = goalsScored;
		this.ballContacts = ballContacts;
		this.possessionTime = possessionTime;
	}

	/**
	 * Get the number of passes made by this player
	 * 
	 * @return the passesMade
	 */
	public int getPassesMade() {
		return passesMade;
	}

	/**
	 * Set the number of passes made by this player
	 * 
	 * @param passesMade
	 *            the passesMade to set
	 */
	public void setPassesMade(int passesMade) {
		this.passesMade = passesMade;
	}

	/**
	 * Get the number of passes missed by this player
	 * 
	 * @return the passesMissed
	 */
	public int getPassesMissed() {
		return passesMissed;
	}

	/**
	 * Set the number of passes missed by this player
	 * 
	 * @param passesMissed
	 *            the passesMissed to set
	 */
	public void setPassesMissed(int passesMissed) {
		this.passesMissed = passesMissed;
	}

	/**
	 * Get the number of passes received by this player
	 * 
	 * @return the passesReceived
	 */
	public int getPassesReceived() {
		return passesReceived;
	}

	/**
	 * Set the number of passes received by this player
	 * 
	 * @param passesReceived
	 *            the passesReceived to set
	 */
	public void setPassesReceived(int passesReceived) {
		this.passesReceived = passesReceived;
	}

	/**
	 * Get the number of tacklings of this player
	 * 
	 * @return the tacklings
	 */
	public int getTacklings() {
		return tacklings;
	}

	/**
	 * Set the number of tacklings of this player
	 * 
	 * @param tacklings
	 *            the tacklings to set
	 */
	public void setTacklings(int tacklings) {
		this.tacklings = tacklings;
	}

	/**
	 * Get the number of successful tacklings of this player
	 * 
	 * @return the tacklesWon
	 */
	public int getTacklesWon() {
		return tacklesWon;
	}

	/**
	 * Set the number of successful tacklings of this player
	 * 
	 * @param tacklesWon
	 *            the tacklesWon to set
	 */
	public void setTacklesWon(int tacklesWon) {
		this.tacklesWon = tacklesWon;
	}

	/**
	 * Get the number of goals scored by this player
	 * 
	 * @return the goalsScored
	 */
	public int getGoalsScored() {
		return goalsScored;
	}

	/**
	 * Set the number of goals scored by this player
	 * 
	 * @param goalsScored
	 *            the goalsScored to set
	 */
	public void setGoalsScored(int goalsScored) {
		this.goalsScored = goalsScored;
	}

	/**
	 * Get the number of ball contacts of this player
	 * 
	 * @return the ballContacts
	 */
	public int getBallContacts() {
		return ballContacts;
	}

	/**
	 * Set the number of ball contacts of this player
	 * 
	 * @param ballContacts
	 *            the ballContacts to set
	 */
	public void setBallContacts(int ballContacts) {
		this.ballContacts = ballContacts;
	}

	/**
	 * Get the total amount of time on which this player had possession of the
	 * ball
	 * 
	 * @return the possessionTime
	 */
	public long getPossessionTime() {
		return possessionTime;
	}

	/**
	 * Set the total amount of time on which this player had possession of the
	 * ball
	 * 
	 * @param possessionTime
	 *            the possessionTime to set
	 */
	public void setPossessionTime(long possessionTime) {
		this.possessionTime = possessionTime;
	}

	/**
	 * Get the ID of this player
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<PlayerStatistic>");

		// ID
		sb.append("<id>");
		sb.append(id);
		sb.append("</id>");

		// passesMade
		sb.append("<passesMade>");
		sb.append(passesMade);
		sb.append("</passesMade>");

		// passesMissed
		sb.append("<passesMissed>");
		sb.append(passesMissed);
		sb.append("</passesMissed>");

		// passesReceived
		sb.append("<passesReceived>");
		sb.append(passesReceived);
		sb.append("</passesReceived>");

		// tacklings
		sb.append("<tacklings>");
		sb.append(tacklings);
		sb.append("</tacklings>");

		// tacklesWon
		sb.append("<tacklesWon>");
		sb.append(tacklesWon);
		sb.append("</tacklesWon>");

		// goalsScored
		sb.append("<goalsScored>");
		sb.append(goalsScored);
		sb.append("</goalsScored>");

		// ballContacts
		sb.append("<ballContacts>");
		sb.append(ballContacts);
		sb.append("</ballContacts>");

		// possessionTime
		sb.append("<possessionTime>");
		sb.append(possessionTime);
		sb.append("</possessionTime>");

		sb.append("</PlayerStatistic>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(PlayerStatistic iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<PlayerStatistic>");

		// ID
		sb.append("<id>");
		sb.append(id);
		sb.append("</id>");

		// passesMade
		if (iNode.getPassesMade() != this.getPassesMade()) {
			c = true;
			sb.append("<passesMade>");
			sb.append(passesMade);
			sb.append("</passesMade>");
		}

		// passesMissed
		if (iNode.getPassesMissed() != this.getPassesMissed()) {
			c = true;
			sb.append("<passesMissed>");
			sb.append(passesMissed);
			sb.append("</passesMissed>");
		}

		// passesReceived
		if (iNode.getPassesReceived() != this.getPassesReceived()) {
			c = true;
			sb.append("<passesReceived>");
			sb.append(passesReceived);
			sb.append("</passesReceived>");
		}

		// tacklings
		if (iNode.getTacklings() != this.getTacklings()) {
			c = true;
			sb.append("<tacklings>");
			sb.append(tacklings);
			sb.append("</tacklings>");
		}

		// tacklesWon
		if (iNode.getTacklesWon() != this.getTacklesWon()) {
			c = true;
			sb.append("<tacklesWon>");
			sb.append(tacklesWon);
			sb.append("</tacklesWon>");
		}

		// goalsScored
		if (iNode.getGoalsScored() != this.getGoalsScored()) {
			c = true;
			sb.append("<goalsScored>");
			sb.append(goalsScored);
			sb.append("</goalsScored>");
		}

		// ballContacts
		if (iNode.getBallContacts() != this.getBallContacts()) {
			c = true;
			sb.append("<ballContacts>");
			sb.append(ballContacts);
			sb.append("</ballContacts>");
		}

		// possessionTime
		if (iNode.getPossessionTime() != this.getPossessionTime()) {
			c = true;
			sb.append("<possessionTime>");
			sb.append(possessionTime);
			sb.append("</possessionTime>");
		}

		if (c) {
			sb.append("</PlayerStatistic>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(PlayerStatistic dest) {
		dest.setBallContacts(ballContacts);
		dest.setGoalsScored(goalsScored);
		dest.setPassesMade(passesMade);
		dest.setPassesMissed(passesMissed);
		dest.setPassesReceived(passesReceived);
		dest.setPossessionTime(possessionTime);
		dest.setTacklesWon(tacklesWon);
		dest.setTacklings(tacklings);
	}

	@Override
	public PlayerStatistic clone() {
		return new PlayerStatistic(id, passesMade, passesReceived, tacklings,
				tacklesWon, goalsScored, ballContacts, possessionTime);
	}

}
