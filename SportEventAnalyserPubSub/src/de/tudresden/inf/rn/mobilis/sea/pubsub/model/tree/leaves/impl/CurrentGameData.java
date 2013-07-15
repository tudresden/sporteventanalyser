package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PlayingTimeInformation;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.DataNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * This <code>CurrentGameData</code> is a concrete <code>DataNode</code>. It
 * holds all data which is about the game in general
 */
public class CurrentGameData extends DataNode<CurrentGameData> {

	/**
	 * The name of this node
	 */
	private static final String NODENAME = "CurrentGameData";

	/**
	 * The <code>PlayingTimeInformation</code> to set all needed information
	 * about the current game time
	 */
	private PlayingTimeInformation playingTimeInformation;

	/**
	 * Constructor for a <code>CurrentGameData</code>
	 */
	public CurrentGameData() {
		playingTimeInformation = new PlayingTimeInformation(0, 0, 0);
	}

	/**
	 * Private <code>CurrentGameData</code> constructor (for cloning purposes)
	 * 
	 * @param playingTimeInformation
	 *            the original <code>playingTimeInformation</code>
	 */
	private CurrentGameData(PlayingTimeInformation playingTimeInformation) {
		this.playingTimeInformation = playingTimeInformation;
	}

	/**
	 * Get the the <code>PlayingTimeInformation</code> <code>Node</code> to set
	 * all relevant informations about the current game time
	 * 
	 * @return the playingTimeInformation <code>Node</code>
	 */
	public PlayingTimeInformation getPlayingTimeInformation() {
		return playingTimeInformation;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String getNodeName() {
		return NODENAME;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<CurrentGameData>");

		// Append playingTimeInformation
		sb.append(playingTimeInformation.toXML());

		sb.append("</CurrentGameData>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(CurrentGameData iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<CurrentGameData>");

		// Append playingTimeInformation
		String s = playingTimeInformation.toPredictiveCodedXML(iNode
				.getPlayingTimeInformation());
		if (s.length() > 0) {
			c = true;
			sb.append(s);
		}

		if (c) {
			sb.append("</CurrentGameData>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(CurrentGameData dest) {
		// Copy playingTimeInformation
		playingTimeInformation.copy(dest.getPlayingTimeInformation());
	}

	@Override
	public CurrentGameData clone() {
		return new CurrentGameData(playingTimeInformation.clone());
	}

}
