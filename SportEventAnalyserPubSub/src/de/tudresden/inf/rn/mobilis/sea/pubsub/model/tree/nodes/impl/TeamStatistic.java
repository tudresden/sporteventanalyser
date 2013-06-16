package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;

public class TeamStatistic extends Node {

	/**
	 * The name of the team
	 */
	private String teamname;

	/**
	 * Percentage of possession of the ball by this team
	 */
	private double ballPossession;

	/**
	 * Percentage of successful passes
	 */
	private double passingAccuracy;

	/**
	 * Constructor for a <code>TeamStatistic</code>
	 * 
	 * @param teamname
	 *            the name of the team
	 * @param ballPossession
	 *            the percentage of possession of the ball by this team
	 * @param passingAccuracy
	 *            the percentage of successful passes
	 */
	public TeamStatistic(String teamname, double ballPossession,
			double passingAccuracy) {
		this.teamname = teamname;
		this.ballPossession = ballPossession;
		this.passingAccuracy = passingAccuracy;
	}

	/**
	 * Get the percentage of possession of the ball by this team
	 * 
	 * @return the ballPossession
	 */
	public double getBallPossession() {
		return ballPossession;
	}

	/**
	 * Set the percentage of possession of the ball by this team
	 * 
	 * @param ballPossession
	 *            the ballPossession to set
	 */
	public void setBallPossession(double ballPossession) {
		this.ballPossession = ballPossession;
	}

	/**
	 * Get the percentage of successful passes
	 * 
	 * @return the passingAccuracy
	 */
	public double getPassingAccuracy() {
		return passingAccuracy;
	}

	/**
	 * Set the percentage of successful passes
	 * 
	 * @param passingAccuracy
	 *            the passingAccuracy to set
	 */
	public void setPassingAccuracy(double passingAccuracy) {
		this.passingAccuracy = passingAccuracy;
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

		sb.append("<TeamStatistic>");

		// Teamname
		sb.append("<teamname>");
		sb.append(teamname);
		sb.append("</teamname>");

		// BallPossession
		sb.append("<ballPossession>");
		sb.append(ballPossession);
		sb.append("</ballPossession>");

		// PassingAccuracy
		sb.append("<passingAccuracy>");
		sb.append(passingAccuracy);
		sb.append("</passingAccuracy>");

		sb.append("</TeamStatistic>");

		return sb.toString();
	}

	@Override
	public Node clone() {
		return new TeamStatistic(teamname, ballPossession, passingAccuracy);
	}

}
