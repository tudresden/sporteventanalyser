package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces;

public abstract class CollectionNode<T> extends ItemNode<T> implements
		de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils.Iterable {

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
