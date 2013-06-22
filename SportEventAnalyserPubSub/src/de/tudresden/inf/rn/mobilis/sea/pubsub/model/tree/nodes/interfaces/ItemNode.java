package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * An <code>ItemNode</code> is a <code>Node</code> which is created within the
 * PubSub-tree (on the XMPP-server). Therefore it needs to implement the
 * <code>de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils.Iterable</code>
 * <code>interface</code> (so a
 * <code>de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils.Iterator</code>
 * may visit this node)
 * 
 * @param <T>
 *            generic parameter to declare the type of this node
 */
public abstract class ItemNode<T> extends Node<T> implements
		de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils.Iterable {

	/**
	 * Visitors may visit concrete <code>ItemNode</code>s!
	 * 
	 * @param visitor
	 *            <code>Visitor</code> to visit this <code>ItemNode</code>
	 */
	public abstract void accept(Visitor visitor);

	/**
	 * Get the name of this concrete <code>ItemNode</code>
	 * 
	 * @return the name of this <code>ItemNode</code>
	 */
	public abstract String getNodeName();

}
