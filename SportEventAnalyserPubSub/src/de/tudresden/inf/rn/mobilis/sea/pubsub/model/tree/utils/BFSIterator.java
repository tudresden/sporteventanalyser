package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils;

import java.util.LinkedList;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticsFacade;

/**
 * Concrete <code>Iterator</code> which uses the breadth first search (DFS)
 * method to chop the PubSub-tree
 */
public class BFSIterator extends Iterator {

	/**
	 * The <code>StatisticsFacade</code> which is used to access the model
	 */
	private StatisticsFacade statistics;

	/**
	 * Constructor for a <code>BFSIterator</code>
	 * 
	 * @param statistics
	 *            <code>StatisticsFacade</code> to get the current
	 *            <code>StatisticCollection</code> from
	 */
	public BFSIterator(StatisticsFacade statistics) {
		this.statistics = statistics;
		this.reset();
	}

	/**
	 * Chops tree using BFS and lines it up in a <code>LinkedList</code>
	 * 
	 * @param node
	 *            <code>Iterable</code> <code>Node</code> to start DFS
	 */
	private void bfs(Iterable node) {
		if (node != null) {
			LinkedList<Iterable> queue = new LinkedList<Iterable>();
			queue.add(node);
			while (!queue.isEmpty()) {
				node = queue.pop();
				for (Iterable s : node.getChildren()) {
					list.add(s);
					queue.add(s);
				}
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		bfs(statistics.getCurrentStatistics());
	}
}
