package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.interfaces;

/**
 * This is an auxiliary <code>interface</code> to declare some methods which may
 * be done on a <code>CurrentGameData</code> <code>Node</code>
 */
public interface ICurrentGameData {

	/**
	 * Set the current playing time of the game. The format should fit with
	 * MM:SS (Minutes:Seconds)
	 * 
	 * @param playingTime
	 *            the current playing time of the game
	 */
	public void setPlayingTime(String playingTime);

	/**
	 * Set the additional time to be played in this half
	 * 
	 * @param additionalTime
	 *            the additional time to be played in this half
	 */
	public void setAdditionalTime(int additionalTime);

}
