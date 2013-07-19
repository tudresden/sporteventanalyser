package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl;

import java.util.HashMap;
import java.util.Map;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PlayerHeatMap;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.TeamHeatMap;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.DataNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * This <code>CurrentHeatMapData</code> is a concrete <code>DataNode</code>. It
 * holds all heat maps (both: <code>TeamHeatMap</code>s and
 * <code>PlayerHeatMap</code>s!)
 */
public class CurrentHeatMapData extends DataNode<CurrentHeatMapData> {

	/**
	 * The name of this node
	 */
	private static final String NODENAME = "CurrentHeatMapData";

	/**
	 * A <code>Map</code> which holds all known <code>PlayerHeatMap</code>s
	 * (key: ID of the player, value: concrete <code>PlayerHeatMap</code>)
	 */
	private Map<Integer, PlayerHeatMap> playerHeatMaps;

	/**
	 * The <code>TeamHeatMap</code> of the first team
	 */
	private TeamHeatMap teamA;

	/**
	 * The <code>TeamHeatMap</code> of the second team
	 */
	private TeamHeatMap teamB;

	/**
	 * Constructor for a <code>CurrentHeatMapData</code>
	 */
	public CurrentHeatMapData() {
		playerHeatMaps = new HashMap<Integer, PlayerHeatMap>();
	}

	/**
	 * Private <code>CurrentHeatMapData</code> constructor (for cloning
	 * purposes)
	 * 
	 * @param teamA
	 *            the original <code>TeamHeatMap</code> of the first team
	 * @param teamB
	 *            the original <code>TeamHeatMap</code> of the second team
	 */
	private CurrentHeatMapData(TeamHeatMap teamA, TeamHeatMap teamB) {
		playerHeatMaps = new HashMap<Integer, PlayerHeatMap>();
		this.teamA = teamA;
		this.teamB = teamB;
	}

	/**
	 * Registers a new <code>PlayerHeatMap</code> to this
	 * <code>CurrentHeatMapData</code>. All registered
	 * <code>PlayerHeatMap</code>s are frequently published
	 * 
	 * @param playerHeatMap
	 *            the <code>PlayerHeatMap</code> which should be published
	 */
	public void registerPlayer(PlayerHeatMap playerHeatMap) {
		playerHeatMaps.put(playerHeatMap.getID(), playerHeatMap);
	}

	/**
	 * Register <code>TeamHeatMap</code>s for both teams
	 * 
	 * @param firstTeam
	 *            the <code>TeamHeatMap</code> of the first team
	 * @param secondTeam
	 *            the <code>TeamHeatMap</code> of the second team
	 */
	public void registerTeams(TeamHeatMap firstTeam, TeamHeatMap secondTeam) {
		teamA = firstTeam;
		teamB = secondTeam;
	}

	/**
	 * Get the <code>PlayerHeatMap</code> of the player with the specified id
	 * 
	 * @param id
	 *            the ID of the <code>PlayerHeatMap</code>
	 * @return the corresponding <code>PlayerHeatMap</code> or <code>null</code>
	 *         if no <code>PlayerHeatMap</code> exists with such an ID
	 */
	public PlayerHeatMap getPlayerHeatMap(int id) {
		return playerHeatMaps.get(id);
	}

	/**
	 * Get the <code>TeamHeatMap</code> of the team which does have this
	 * teamname
	 * 
	 * @param teamname
	 *            the name of the team which <code>TeamHeatMap</code> should be
	 *            returned
	 * @return the <code>TeamHeatMap</code> of this team or <code>null</code> if
	 *         no team does have such a teamname
	 */
	public TeamHeatMap getTeamHeatMap(String teamname) {
		if (teamA == null)
			return null;
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
