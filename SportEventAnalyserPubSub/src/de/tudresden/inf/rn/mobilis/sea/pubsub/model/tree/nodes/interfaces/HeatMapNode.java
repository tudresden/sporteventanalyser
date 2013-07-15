package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces;

/**
 * This <code>HeatMapNode</code> is an auxiliary <code>Node</code> to handle a
 * concrete heat map
 * 
 * @param <T>
 *            generic parameter to declare the type of this node
 */
public abstract class HeatMapNode<T extends HeatMapNode<T>> extends Node<T> {

	/**
	 * The current heat map (size: width * height)
	 */
	private int[][] map;

	/**
	 * Constructor for a <code>HeatMapNode</code>
	 * 
	 * @param map
	 *            the heat map (may be empty, but at least it should have a
	 *            concrete width and height)
	 */
	public HeatMapNode(int[][] map) {
		this.map = map;
	}

	/**
	 * Get the map
	 * 
	 * @return the map
	 */
	public int[][] getMap() {
		return map;
	}

	/**
	 * Set the map
	 * 
	 * @param map
	 *            the map to set
	 */
	public void setMap(int[][] map) {
		this.map = map;
	}

	/**
	 * Deeply clone a two-dimensional array of <code>int</code>s
	 * 
	 * @param map
	 *            the <code>int[][]</code> which should be cloned
	 * @return the cloned map
	 */
	protected int[][] deepCloneMap(int[][] map) {
		if (map == null)
			return null;
		int[][] clone = new int[map.length][];
		for (int r = 0; r < map.length; r++) {
			int[] f = new int[map[r].length];
			System.arraycopy(map[r], 0, f, 0, map[r].length);
			clone[r] = f;
		}
		return clone;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<HeatMap>");

		// Append map (JSON!)
		sb.append("[");
		for (int[] row : map) {
			sb.append("[");
			for (int value : row) {
				sb.append(value);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("],");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");

		sb.append("</HeatMap>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(
			@SuppressWarnings("rawtypes") HeatMapNode iNode) {
		StringBuilder sb = new StringBuilder();

		// TODO: Not implemented yet

		return sb.toString();
	}

	@Override
	public void copy(@SuppressWarnings("rawtypes") HeatMapNode dest) {
		// Set Map
		int[][] destMap = dest.getMap();
		for (int r = 0; r < map.length; r++) {
			System.arraycopy(map[r], 0, destMap[r], 0, map[r].length);
		}
		dest.setMap(map);
	}

}
