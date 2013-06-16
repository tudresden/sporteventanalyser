package de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs;

import org.jivesoftware.smackx.pubsub.NodeType;

/**
 * This is a concrete <code>CommonSubmitNodeConfig</code> which is used as
 * <code>NodeType.leaf</code>.
 */
public class NPLeafConfig extends CommonSubmitNodeConfig {

	/**
	 * Constructor for this <code>LeafForm</code>
	 * 
	 * @param title
	 *            human readable title for the node
	 * @param collection
	 *            id of collection node which the node is affiliated with
	 */
	public NPLeafConfig(String title, String collection) {
		super(title);
		// Set type of node (NodeType.leaf!)
		this.setNodeType(NodeType.leaf);
		// Just publish items (there is no need that nodes are persisted => Anyone may start using this service by waiting for the first I-Frame!)
		this.setPersistentItems(false);
		// Set id of collection node
		this.setCollection(collection);
	}

}
