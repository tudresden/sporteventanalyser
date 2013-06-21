package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticsFacade;

/**
 * Concrete <code>Iterator</code> through a syntax tree with depth first search
 * (DFS) method
 */
public class DFSIterator extends Iterator {

	/**
	 * The <code>StatisticsFacade</code> which is used to access the model
	 */
	private StatisticsFacade statistics;

	/**
	 * Constructor for a <code>DFSIterator</code>
	 * 
	 * @param statistics
	 *            <code>StatisticsFacade</code> to get the current
	 *            <code>StatisticCollection</code> from
	 */
	public DFSIterator(StatisticsFacade statistics) {
		this.statistics = statistics;
		this.reset();
	}

	/**
	 * Chop tree using DFS and line it up in a <code>LinkedList</code>
	 * 
	 * @param node
	 *            <code>Iterable</code> <code>Node</code> to start DFS
	 */
	private void dfs(Iterable node) {
		if (node != null) {
			list.add(node);
			for (Iterable s : node.getChildren()) {
				dfs(s);
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		dfs(statistics.getCurrentStatistics());
	}
}
