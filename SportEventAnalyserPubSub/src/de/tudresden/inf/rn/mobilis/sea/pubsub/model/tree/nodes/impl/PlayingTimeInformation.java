package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;

/**
 * A <code>PlayingTimeInformation</code> may be used to set all needed
 * information about the current game time
 */
public class PlayingTimeInformation extends Node<PlayingTimeInformation> {

	/**
	 * <code>String</code> representation of the current playing time
	 */
	private String playingTime;

	/**
	 * Additional time to be played in this half
	 */
	private int additionalTime;

	/**
	 * Constructor for a <code>PlayingTimeInformation</code>
	 * 
	 * @param playingTime
	 *            the current playing time
	 * @param additionalTime
	 *            the additional time to be played in this half
	 */
	public PlayingTimeInformation(String playingTime, int additionalTime) {
		this.playingTime = playingTime;
		this.additionalTime = additionalTime;
	}

	/**
	 * Get the current playing time
	 * 
	 * @return the playingTime
	 */
	public String getPlayingTime() {
		return playingTime;
	}

	/**
	 * Set the current playing time
	 * 
	 * @param playingTime
	 *            the playingTime to set
	 */
	public void setPlayingTime(String playingTime) {
		this.playingTime = playingTime;
	}

	/**
	 * Get the additional time to be played in this half
	 * 
	 * @return the additionalTime
	 */
	public int getAdditionalTime() {
		return additionalTime;
	}

	/**
	 * Set the additional time to be played in this half
	 * 
	 * @param additionalTime
	 *            the additionalTime to set
	 */
	public void setAdditionalTime(int additionalTime) {
		this.additionalTime = additionalTime;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<PlayingTimeInformation>");

		// playingTime
		sb.append("<playingTime>");
		sb.append(playingTime);
		sb.append("</playingTime>");

		// additionalTime
		sb.append("<additionalTime>");
		sb.append(additionalTime);
		sb.append("</additionalTime>");

		sb.append("</PlayingTimeInformation>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(PlayingTimeInformation iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<PlayingTimeInformation>");

		// playingTime
		if (!iNode.getPlayingTime().equals(this.getPlayingTime())) {
			c = true;
			sb.append("<playingTime>");
			sb.append(playingTime);
			sb.append("</playingTime>");
		}

		// additionalTime
		if (iNode.getAdditionalTime() != this.getAdditionalTime()) {
			c = true;
			sb.append("<additionalTime>");
			sb.append(additionalTime);
			sb.append("</additionalTime>");
		}

		if (c) {
			sb.append("</PlayingTimeInformation>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(PlayingTimeInformation dest) {
		dest.setPlayingTime(playingTime);
		dest.setAdditionalTime(additionalTime);
	}

	@Override
	public PlayingTimeInformation clone() {
		return new PlayingTimeInformation(playingTime, additionalTime);
	}

}
