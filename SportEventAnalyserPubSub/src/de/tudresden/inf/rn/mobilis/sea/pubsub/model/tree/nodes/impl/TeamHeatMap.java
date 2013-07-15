package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.HeatMapNode;

/**
 * A <code>TeamHeatMap</code> is a concrete <code>HeatMapNode</code> to handle
 * the heat map of a team
 */
public class TeamHeatMap extends HeatMapNode<TeamHeatMap> {

	/**
	 * The name of the team
	 */
	private String teamname;

	/**
	 * Constructor for a <code>TeamHeatMap</code>
	 * 
	 * @param teamname
	 *            the name of the team
	 * @param map
	 *            the heat map (may be empty, but at least it should have a
	 *            concrete width and height)
	 */
	public TeamHeatMap(String teamname, int[][] map) {
		super(map);
		this.teamname = teamname;
	}

	/**
	 * Get the name of this team
	 * 
	 * @return the teamname
	 */
	public String getTeamname() {
		return teamname;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<TeamHeatMap>");

		// ID
		sb.append("<teamname>");
		sb.append(teamname);
		sb.append("</teamname>");

		// Append super
		sb.append(super.toXML());

		sb.append("</TeamHeatMap>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(TeamHeatMap iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<TeamHeatMap>");

		// ID
		sb.append("<teamname>");
		sb.append(teamname);
		sb.append("</teamname>");

		// Append super
		String s = super.toPredictiveCodedXML(iNode);
		if (s.length() > 0) {
			c = true;
			sb.append(s);
		}

		if (c) {
			sb.append("</TeamHeatMap>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(TeamHeatMap dest) {
		// Copy super
		super.copy(dest);
	}

	@Override
	public TeamHeatMap clone() {
		return new TeamHeatMap(teamname, deepCloneMap(this.getMap()));
	}

}
