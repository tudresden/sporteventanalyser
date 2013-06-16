package de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs;

import org.jivesoftware.smackx.pubsub.NodeType;

/**
 * This is a concrete <code>CommonSubmitNodeConfig</code> which is used as
 * <code>NodeType.leaf</code>.
 */
public class PLeafConfig extends CommonSubmitNodeConfig {

	/**
	 * Constructor for this <code>LeafForm</code>
	 * 
	 * @param title
	 *            human readable title for the node
	 * @param collection
	 *            id of collection node which the node is affiliated with
	 */
	public PLeafConfig(String title, String collection) {
		super(title);
		// Set type of node (NodeType.leaf!)
		this.setNodeType(NodeType.leaf);
		// Persisted items (an item which items should be persisted should only publish a small number of items!)
		this.setPersistentItems(true);
		// Set id of collection node
		this.setCollection(collection);
	}

}
