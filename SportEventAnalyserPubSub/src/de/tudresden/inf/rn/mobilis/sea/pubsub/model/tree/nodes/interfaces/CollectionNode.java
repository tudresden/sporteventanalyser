package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces;

/**
 * A <code>CollectionNode</code> should always be used when the
 * <code>ItemNode</code> is of type collection. Any collection do not need to be
 * serialized into xml code (therefore methods <code>Node.toXML()</code> and
 * <code>Node.toPredictiveCodedXML(T iNode)</code> are overridden
 * 
 * @param <T>
 *            generic parameter to declare the type of this node
 */
public abstract class CollectionNode<T> extends ItemNode<T> {

	@Override
	public String toXML() {
		// Nothing to see here! This method should never be called
		return "";
	}

	@Override
	public String toPredictiveCodedXML(T iNode) {
		// Nothing to see here! This method should never be called
		return "";
	}

}
