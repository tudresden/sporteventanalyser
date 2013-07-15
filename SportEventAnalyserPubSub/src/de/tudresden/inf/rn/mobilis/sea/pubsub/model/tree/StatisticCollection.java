package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree;

import java.util.LinkedList;
import java.util.List;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentGameData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentHeatMapData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPlayerData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPositionData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPrognosisData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentTeamData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.CollectionNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.ItemNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * This <code>StatisticCollection</code> is the root of the PubSub-tree. It does
 * have several concrete <code>DataNode</code> children where new data may be
 * set (you may use this <code>StatisticCollection</code> to subscribe to a
 * <code>CollectionNode</code> to get all data which is published within this
 * subtree)
 */
public class StatisticCollection extends CollectionNode<StatisticCollection> {

	/**
	 * The name of this node
	 */
	private static final String NODENAME = "StatisticCollection";

	/**
	 * A <code>CurrentPositionData</code> statistic node
	 */
	private CurrentPositionData currentPositionData;

	/**
	 * A <code>CurrentPlayerData</code> statistic node
	 */
	private CurrentPlayerData currentPlayerData;

	/**
	 * A <code>CurrentTeamData</code> statistic node
	 */
	private CurrentTeamData currentTeamData;

	/**
	 * A <code>CurrentHeatMapData</code> statistic node
	 */
	private CurrentHeatMapData currentHeatMapData;

	/**
	 * A <code>CurrentPrognosisData</code> prediction node
	 */
	private CurrentPrognosisData currentPrognosisData;

	/**
	 * A <code>CurrentGameData</code> information node
	 */
	private CurrentGameData currentGameData;

	/**
	 * Constructor for this <code>StatisticCollection</code>
	 */
	public StatisticCollection() {
		this.currentPositionData = new CurrentPositionData();
		this.currentPlayerData = new CurrentPlayerData();
		this.currentTeamData = new CurrentTeamData();
		this.currentHeatMapData = new CurrentHeatMapData();
		this.currentPrognosisData = new CurrentPrognosisData();
		this.currentGameData = new CurrentGameData();
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
	 * @param currentHeatMapData
	 *            the original <code>CurrentHeatMapData</code>
	 * @param currentGameData
	 *            the original <code>CurrentGameData</code>
	 */
	private StatisticCollection(CurrentPositionData currentPositionData,
			CurrentPlayerData currentPlayerData,
			CurrentTeamData currentTeamData,
			CurrentHeatMapData currentHeatMapData,
			CurrentPrognosisData currentPrognosisData,
			CurrentGameData currentGameData) {
		this.currentPositionData = currentPositionData;
		this.currentPlayerData = currentPlayerData;
		this.currentTeamData = currentTeamData;
		this.currentHeatMapData = currentHeatMapData;
		this.currentPrognosisData = currentPrognosisData;
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

	/**
	 * Get the <code>CurrentHeatMapData</code>
	 * 
	 * @return the currentHeatMapData
	 */
	public CurrentHeatMapData getCurrentHeatMapData() {
		return currentHeatMapData;
	}

	/**
	 * Get the <code>CurrentPrognosisData</code>
	 * 
	 * @return the currentPrognosisData
	 */
	public CurrentPrognosisData getCurrentPrognosisData() {
		return currentPrognosisData;
	}

	/**
	 * Get the <code>CurrentGameData</code>
	 * 
	 * @return the currentGameData
	 */
	public CurrentGameData getCurrentGameData() {
		return currentGameData;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public List<ItemNode<?>> getChildren() {
		List<ItemNode<?>> list = new LinkedList<ItemNode<?>>();
		list.add(currentPositionData);
		list.add(currentPlayerData);
		list.add(currentPrognosisData);
		list.add(currentHeatMapData);
		list.add(currentTeamData);
		list.add(currentGameData);
		return list;
	}

	@Override
	public String getNodeName() {
		return NODENAME;
	}

	@Override
	public void copy(StatisticCollection dest) {
		// Copy CurrentPositionData
		currentPositionData.copy(dest.getCurrentPositionData());

		// Copy CurrentPlayerData
		currentPlayerData.copy(dest.getCurrentPlayerData());

		// Copy CurrentTeamData
		currentTeamData.copy(dest.getCurrentTeamData());

		// Copy CurrentHeatMapData
		currentHeatMapData.copy(dest.getCurrentHeatMapData());

		// Copy CurrentPrognosisData
		currentPrognosisData.copy(dest.getCurrentPrognosisData());

		// Copy CurrentGameData
		currentGameData.copy(dest.getCurrentGameData());
	}

	@Override
	public StatisticCollection clone() {
		return new StatisticCollection(this.currentPositionData.clone(),
				this.currentPlayerData.clone(), this.currentTeamData.clone(),
				this.currentHeatMapData.clone(),
				this.currentPrognosisData.clone(), this.currentGameData.clone());
	}

}
