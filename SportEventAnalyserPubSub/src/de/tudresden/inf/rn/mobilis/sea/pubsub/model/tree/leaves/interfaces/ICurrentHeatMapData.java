package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.interfaces;

/**
 * This is an auxiliary <code>interface</code> to declare some methods which may
 * be done on a <code>CurrentHeatMapData</code> <code>Node</code>
 */
public interface ICurrentHeatMapData {

	/**
	 * Register a new heatmap for a player (specified by the id of the player).
	 * It is mandatory to call this method before setting any data in the
	 * heatmap with such an id
	 * 
	 * @param id
	 *            the id of the player
	 * @param width
	 *            the width of the heatmap (fields on the x-axis)
	 * @param height
	 *            the height of the heatmap (fields on the y-axis)
	 */
	public void registerPlayerHeatMap(int id, int width, int height);

	/**
	 * Register a new heatmap for a team (specified by the name of the team). It
	 * is mandatory to call this method before setting any data in the heatmap
	 * with such a name
	 * 
	 * @param firstTeamname
	 *            the name of the first team
	 * @param secondTeamname
	 *            the name of the second team
	 * @param width
	 *            the width of the heatmap (fields on the x-axis)
	 * @param height
	 *            the height of the heatmap (fields on the y-axis)
	 */
	public void registerTeamHeatMap(String firstTeamname,
			String secondTeamname, int width, int height);

	/**
	 * Set a value in the heatmap at the specified position. If the x or y value
	 * is not within the grid of the heatmap no changes will be applied. The
	 * heatmap must be registered first (
	 * <code>StatisticsFacade.registerPlayerHeatMap(id,width,height)</code>).
	 * Otherwise nothing will be published
	 * 
	 * @param id
	 *            the id of the player
	 * @param x
	 *            the x position within the grid (where to set the new value)
	 * @param y
	 *            the y position within the grid (where to set the new value)
	 * @param value
	 *            the value which should be set
	 */
	public void setValueInHeatMap(int id, int x, int y, int value);

	/**
	 * Set a value in the heatmap at the specified position. If the x or y value
	 * is not within the grid of the heatmap no changes will be applied. The
	 * heatmap must be registered first (
	 * <code>StatisticsFacade.registerTeamHeatMap(id,width,height)</code>).
	 * Otherwise nothing will be published
	 * 
	 * @param teamname
	 *            the name of the first team
	 * @param x
	 *            the x position within the grid (where to set the new value)
	 * @param y
	 *            the y position within the grid (where to set the new value)
	 * @param value
	 *            the value which should be set
	 */
	public void setValueInHeatMap(String teamname, int x, int y, int value);

}
