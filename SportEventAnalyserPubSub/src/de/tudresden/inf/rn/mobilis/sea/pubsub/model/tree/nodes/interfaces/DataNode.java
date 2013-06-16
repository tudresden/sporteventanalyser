package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces;

import java.util.LinkedList;
import java.util.List;

/**
 * A <code>DataNode</code> does only hold data. Therefore it is not
 * <code>Iterable</code>
 */
public abstract class DataNode extends ItemNode {

	@Override
	public List<ItemNode> getChildren() {
		return new LinkedList<ItemNode>();
	}

}
