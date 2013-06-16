package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree;

import java.util.LinkedList;
import java.util.List;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPlayerData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPositionData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentTeamData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.CollectionNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.ItemNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

public class StatisticCollection extends CollectionNode {

	private static final String NODENAME = "StatisticCollection";

	private CurrentPositionData currentPositionData;

	private CurrentPlayerData currentPlayerData;

	private CurrentTeamData currentTeamData;

	public StatisticCollection() {
		this.currentPositionData = new CurrentPositionData();
		this.currentPlayerData = new CurrentPlayerData();
		this.currentTeamData = new CurrentTeamData();
	}

	/**
	 * Private <code>StatisticCollection</code> constructor (for cloning
	 * purposes)
	 * 
	 * @param currentPositionData
	 *            the original <code>CurrentPositionData</code>
	 * @param currentPlayerData
	 *            the original <code>CurrentPlayerData</code>
	 * @param currentTeamData
	 *            the original <code>CurrentTeamData</code>
	 */
	private StatisticCollection(CurrentPositionData currentPositionData,
			CurrentPlayerData currentPlayerData, CurrentTeamData currentTeamData) {
		this.currentPositionData = currentPositionData;
		this.currentPlayerData = currentPlayerData;
		this.currentTeamData = currentTeamData;
	}

	/**
	 * Get the <code>CurrentPositionData</code>
	 * 
	 * @return the currentPositionData
	 */
	public CurrentPositionData getCurrentPositionData() {
		return currentPositionData;
	}

	/**
	 * Get the <code>CurrentPlayerData</code>
	 * 
	 * @return the currentPlayerData
	 */
	public CurrentPlayerData getCurrentPlayerData() {
		return currentPlayerData;
	}

	/**
	 * Get the <code>CurrentTeamData</code>
	 * 
	 * @return the currentTeamData
	 */
	public CurrentTeamData getCurrentTeamData() {
		return currentTeamData;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public List<ItemNode> getChildren() {
		List<ItemNode> list = new LinkedList<ItemNode>();
		list.add(currentPositionData);
		list.add(currentPlayerData);
		list.add(currentTeamData);
		return list;
	}

	@Override
	public String getNodeName() {
		return NODENAME;
	}

	@Override
	public Node clone() {
		return new StatisticCollection(
				(CurrentPositionData) this.currentPositionData.clone(),
				(CurrentPlayerData) this.currentPlayerData.clone(),
				(CurrentTeamData) this.currentTeamData.clone());
	}

}
