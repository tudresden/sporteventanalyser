package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils;

/**
 * Concrete <code>Iterator</code> through a syntax tree with depth first search
 * (DFS) method
 */
public class DFSIterator extends Iterator {

	/**
	 * Constructor for a <code>DFSIterator</code>
	 * 
	 * @param node
	 *            <code>Iterable</code> to start DFS
	 */
	public DFSIterator(Iterable node) {
		dfs(node);
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
}
