package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils;

import java.util.List;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.ItemNode;

/**
 * <code>Interface</code> to determine that an <code>Object</code> is iterable
 * by an <code>Iterator</code>
 */
public interface Iterable {

	/**
	 * Return children as sequence
	 * 
	 * @return <code>Collection</code> of <code>Iterable</code> children
	 */
	public List<ItemNode> getChildren();

}
