package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;

/**
 * A <code>PlayingTimeInformation</code> may be used to set all needed
 * information about the current game time
 */
public class PlayingTimeInformation extends Node<PlayingTimeInformation> {

	/**
	 * Currently played minutes
	 */
	private int playedMinutes;

	/**
	 * Currently played seconds
	 */
	private int playedSeconds;

	/**
	 * Additional time to be played in this half
	 */
	private int additionalTime;

	/**
	 * Constructor for a <code>PlayingTimeInformation</code>
	 * 
	 * @param playedMinutes
	 *            the currently played minutes
	 * @param playedSeconds
	 *            the currently played seconds
	 * @param additionalTime
	 *            the additional time to be played in this half
	 */
	public PlayingTimeInformation(int playedMinutes, int playedSeconds,
			int additionalTime) {
		this.playedMinutes = playedMinutes;
		this.playedSeconds = playedSeconds;
		this.additionalTime = additionalTime;
	}

	/**
	 * Get the current playing time
	 * 
	 * @return the playingTime
	 */
	public String getPlayingTime() {
		return playedMinutes + ":"
				+ (playedSeconds > 9 ? playedSeconds : "0" + playedSeconds);
	}

	/**
	 * Get the currently played minutes
	 * 
	 * @return the playedMinutes
	 */
	public int getPlayedMinutes() {
		return playedMinutes;
	}

	/**
	 * Set the currently played minutes
	 * 
	 * @param playedMinutes
	 *            the playedMinutes to set
	 */
	public void setPlayedMinutes(int playedMinutes) {
		this.playedMinutes = playedMinutes;
	}

	/**
	 * Get the currently played seconds
	 * 
	 * @return the playedSeconds
	 */
	public int getPlayedSeconds() {
		return playedSeconds;
	}

	/**
	 * Set the currently played seconds
	 * 
	 * @param playedSeconds
	 *            the playedSeconds to set
	 */
	public void setPlayedSeconds(int playedSeconds) {
		this.playedSeconds = playedSeconds;
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
		sb.append(getPlayingTime());
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
		if (iNode.getPlayedMinutes() != this.getPlayedMinutes()
				|| iNode.getPlayedSeconds() != this.getPlayedSeconds()) {
			c = true;
			sb.append("<playingTime>");
			sb.append(getPlayingTime());
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
		dest.setPlayedMinutes(playedMinutes);
		dest.setPlayedSeconds(playedSeconds);
		dest.setAdditionalTime(additionalTime);
	}

	@Override
	public PlayingTimeInformation clone() {
		return new PlayingTimeInformation(playedMinutes, playedSeconds,
				additionalTime);
	}

}
