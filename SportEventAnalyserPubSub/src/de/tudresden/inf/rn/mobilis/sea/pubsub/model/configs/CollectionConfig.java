package de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs;

import org.jivesoftware.smackx.pubsub.NodeType;

/**
 * This is a concrete <code>CommonSubmitNodeConfig</code> which is used as
 * <code>NodeType.collection</code>.
 */
public class CollectionConfig extends CommonSubmitNodeConfig {

	/**
	 * Constructor for this <code>CollectionForm</code>
	 * 
	 * @param title
	 *            human readable title for the node
	 */
	public CollectionConfig(String title) {
		super(title);
		// Set type of node (NodeType.collection!)
		this.setNodeType(NodeType.collection);
	}

}
