package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl;

import java.util.HashMap;
import java.util.Map;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PlayerHeatMap;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.TeamHeatMap;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.DataNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

public class CurrentHeatMapData extends DataNode<CurrentHeatMapData> {

	/**
	 * The name of this node
	 */
	private static final String NODENAME = "CurrentHeatMapData";

	private Map<Integer, PlayerHeatMap> playerHeatMaps;

	private TeamHeatMap teamA;

	private TeamHeatMap teamB;

	public CurrentHeatMapData() {
		playerHeatMaps = new HashMap<Integer, PlayerHeatMap>();
	}

	private CurrentHeatMapData(TeamHeatMap teamA, TeamHeatMap teamB) {
		playerHeatMaps = new HashMap<Integer, PlayerHeatMap>();
		this.teamA = teamA;
		this.teamB = teamB;
	}

	public void registerPlayer(PlayerHeatMap playerHeatMap) {
		playerHeatMaps.put(playerHeatMap.getID(), playerHeatMap);
	}

	public void registerTeams(TeamHeatMap firstTeam, TeamHeatMap secondTeam) {
		teamA = firstTeam;
		teamB = secondTeam;
	}

	public PlayerHeatMap getPlayerHeatMap(int id) {
		return playerHeatMaps.get(id);
	}

	public TeamHeatMap getTeamHeatMap(String teamname) {
		if (teamA.getTeamname().equals(teamname))
			return teamA;
		if (teamB.getTeamname().equals(teamname))
			return teamB;
		return null;
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

		sb.append("<CurrentHeatMapData>");

		// Append PlayerHeatMap
		for (PlayerHeatMap playerHeatMap : playerHeatMaps.values()) {
			sb.append(playerHeatMap.toXML());
		}

		// Append TeamHeatMap
		if (teamA != null) {
			sb.append(teamA.toXML());
			sb.append(teamB.toXML());
		}

		sb.append("</CurrentHeatMapData>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(CurrentHeatMapData iNode) {
		// TODO: Not implemented yet
		return null;
	}

	@Override
	public void copy(CurrentHeatMapData dest) {
		// Copy TeamHeatMaps
		if (teamA != null) {
			TeamHeatMap destTeamA;
			if ((destTeamA = dest.getTeamHeatMap(teamA.getTeamname())) == null) {
				dest.registerTeams(teamA.clone(), teamB.clone());
			} else {
				teamA.copy(destTeamA);
				teamB.copy(dest.getTeamHeatMap(teamB.getTeamname()));
			}
		}
		// Copy PlayerHeatMaps
		PlayerHeatMap destPlayerHeatMap;
		for (PlayerHeatMap playerHeatMap : playerHeatMaps.values()) {
			if ((destPlayerHeatMap = dest.getPlayerHeatMap(playerHeatMap
					.getID())) == null) {
				dest.registerPlayer(playerHeatMap.clone());
			} else {
				playerHeatMap.copy(destPlayerHeatMap);
			}
		}
	}

	@Override
	public CurrentHeatMapData clone() {
		CurrentHeatMapData clone = new CurrentHeatMapData(teamA.clone(),
				teamB.clone());
		for (PlayerHeatMap playerHeatMap : playerHeatMaps.values()) {
			clone.registerPlayer(playerHeatMap);
		}
		return clone;
	}

}
