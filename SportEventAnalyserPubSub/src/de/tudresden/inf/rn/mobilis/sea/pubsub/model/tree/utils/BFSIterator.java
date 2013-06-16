package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils;

import java.util.LinkedList;

/**
 * Concrete <code>Iterator</code> through a syntax tree with breadth first
 * search (DFS) method
 */
public class BFSIterator extends Iterator {

	/**
	 * Constructor for a <code>BFSIterator</code>
	 * 
	 * @param node
	 *            <code>Iterable</code> to start BFS
	 */
	public BFSIterator(Iterable node) {
		bfs(node);
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
}
