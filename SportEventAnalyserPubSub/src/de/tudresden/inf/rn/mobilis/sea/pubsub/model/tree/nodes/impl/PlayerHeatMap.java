package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.HeatMapNode;

public class PlayerHeatMap extends HeatMapNode<PlayerHeatMap> {

	/**
	 * ID of this player
	 */
	private int id;

	public PlayerHeatMap(int id, int[][] map) {
		super(map);
		this.id = id;
	}

	/**
	 * Get the ID of this player
	 * 
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<PlayerHeatMap>");

		// ID
		sb.append("<id>");
		sb.append(id);
		sb.append("</id>");

		// Append super
		sb.append(super.toXML());

		sb.append("</PlayerHeatMap>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(PlayerHeatMap iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<PlayerHeatMap>");

		// ID
		sb.append("<id>");
		sb.append(id);
		sb.append("</id>");

		// Append super
		String s = super.toPredictiveCodedXML(iNode);
		if (s.length() > 0) {
			c = true;
			sb.append(s);
		}

		if (c) {
			sb.append("</PlayerHeatMap>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(PlayerHeatMap dest) {
		// Copy super
		super.copy(dest);
	}

	@Override
	public PlayerHeatMap clone() {
		return new PlayerHeatMap(id, deepCloneMap(this.getMap()));
	}

}
