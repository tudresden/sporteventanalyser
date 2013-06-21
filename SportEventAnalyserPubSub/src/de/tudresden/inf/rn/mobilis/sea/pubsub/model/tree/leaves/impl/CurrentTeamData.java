package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.TeamStatistic;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.DataNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

public class CurrentTeamData extends DataNode<CurrentTeamData> {

	private static final String NODENAME = "CurrentTeamData";

	/**
	 * The <code>TeamStatistic</code> of this team
	 */
	private TeamStatistic teamA, teamB;

	/**
	 * Constructor for a <code>CurrentTeamData</code>
	 */
	public CurrentTeamData() {
		teamA = null;
		teamB = null;
	}

	/**
	 * Private <code>CurrentTeamData</code> constructor (for cloning purposes)
	 * 
	 * @param teamA
	 *            the original <code>TeamStatistic</code> of the first team
	 * @param teamB
	 *            the original <code>TeamStatistic</code> of the second team
	 */
	private CurrentTeamData(TeamStatistic teamA, TeamStatistic teamB) {
		this.teamA = teamA;
		this.teamB = teamB;
	}

	/**
	 * Register teams
	 * 
	 * @param firstTeam
	 *            the <code>TeamStatistic</code> of the first team
	 * @param secondTeam
	 *            the <code>TeamStatistic</code> of the second team
	 */
	public void registerTeams(TeamStatistic firstTeam, TeamStatistic secondTeam) {
		teamA = firstTeam;
		teamB = secondTeam;
	}

	/**
	 * Get the <code>TeamStatistic</code> of the team which does have this
	 * teamname
	 * 
	 * @param teamname
	 *            the name of the team which <code>TeamStatistic</code> should
	 *            be returned
	 * @return the <code>TeamStatistic</code> of this team or <code>null</code>
	 *         if no team does have such a teamname
	 */
	public TeamStatistic getTeamStatistic(String teamname) {
		if (teamA.getTeamname().equals(teamname))
			return teamA;
		if (teamB.getTeamname().equals(teamname))
			return teamB;
		return null;
	}

	/**
	 * Get the <code>TeamStatistic</code> of the first team
	 * 
	 * @return the <code>TeamStatistic</code> of the first team
	 */
	public TeamStatistic getTeamStatisticOfFirstTeam() {
		return teamA;
	}

	/**
	 * Get the <code>TeamStatistic</code> of the second team
	 * 
	 * @return the <code>TeamStatistic</code> of the second team
	 */
	public TeamStatistic getTeamStatisticOfSecondTeam() {
		return teamB;
	}

	/**
	 * Get the name of the first team
	 * 
	 * @return the name of the first team
	 */
	public String getNameOfFirstTeam() {
		if (teamA == null)
			return "";
		return teamA.getTeamname();
	}

	/**
	 * Get the name of the second team
	 * 
	 * @return the of the second team
	 */
	public String getNameOfSecondTeam() {
		if (teamB == null)
			return "";
		return teamB.getTeamname();
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

		sb.append("<CurrentTeamData>");

		if (teamA != null) {
			// Append teamA
			sb.append(teamA.toXML());

			// Append teamB
			sb.append(teamB.toXML());
		}

		sb.append("</CurrentTeamData>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(CurrentTeamData iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<CurrentTeamData>");

		String s;
		// Append teamA
		if (teamA != null) {
			if (iNode.getTeamStatisticOfFirstTeam() == null) {
				s = teamA.toXML();
			} else {
				s = teamA.toPredictiveCodedXML(iNode.getTeamStatistic(teamA
						.getTeamname()));
			}
			if (s.length() > 0) {
				c = true;
				sb.append(s);
			}

			// Append teamB
			if (iNode.getTeamStatisticOfSecondTeam() == null) {
				s = teamB.toXML();
			} else {
				s = teamB.toPredictiveCodedXML(iNode.getTeamStatistic(teamB
						.getTeamname()));
			}
			if (s.length() > 0) {
				c = true;
				sb.append(s);
			}
		}

		if (c) {
			sb.append("</CurrentTeamData>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(CurrentTeamData dest) {
		// Copy TeamStatistic
		TeamStatistic destTeamA;
		if (teamA != null) {
			if ((destTeamA = dest.getTeamStatistic(teamA.getTeamname())) == null) {
				dest.registerTeams(teamA.clone(), teamB.clone());
			} else {
				teamA.copy(destTeamA);
				teamB.copy(dest.getTeamStatistic(teamB.getTeamname()));
			}
		}
	}

	@Override
	public CurrentTeamData clone() {
		if (teamA == null)
			return new CurrentTeamData();
		return new CurrentTeamData(teamA.clone(), teamB.clone());
	}

}
