package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.interfaces;

public interface ICurrentTeamData {

	/**
	 * Set the percentage of possession of the ball by this team. The possession
	 * of the other team will automatically be set. The teams must be registered
	 * first (
	 * <code>StatisticsFacade.registerTeams(firstTeamname, secondTeamname)</code>
	 * ). Otherwise nothing will be published
	 * 
	 * @param teamname
	 *            the name of the team which has this percentage of possession
	 * @param ballPossession
	 *            the percentage value of possession of the ball by this team
	 *            (e.g.: 0% equals 0, 49.54% equals 49.54, 100% equals 100)
	 */
	public void setBallPossession(String teamname, double ballPossession);

	/**
	 * Set the passing accuracy of this team. The team must be registered first
	 * (
	 * <code>StatisticsFacade.registerTeams(firstTeamname, secondTeamname)</code>
	 * ). Otherwise nothing will be published
	 * 
	 * @param teamname
	 *            the name of the team which passing accuracy has to be set
	 * @param passingAccuracy
	 *            the percentage value of the passing accuracy by this team
	 *            (e.g.: 0% equals 0, 49.54% equals 49.54, 100% equals 100)
	 */
	public void setPassingAccuracy(String teamname, double passingAccuracy);

}
