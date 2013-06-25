package de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs;

import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ChildrenAssociationPolicy;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.PublishModel;

/**
 * This abstract class sets default values for all nodes which are created
 * within the PubSub-tree of the XMPP-Server
 */
public abstract class CommonSubmitNodeConfig extends ConfigureForm {

	/**
	 * Constructor for this <code>CommonSubmitNodeConfig</code>
	 * 
	 * @param title
	 *            human readable title for the node
	 */
	public CommonSubmitNodeConfig(String title) {
		super(FormType.submit);
		// Set title
		this.setTitle(title);
		// Only publishers may publish items (subscribers should only be able to receive/display data)
		this.setPublishModel(PublishModel.publishers);
		// Only owners may associate children with this node
		this.setChildrenAssociationPolicy(ChildrenAssociationPolicy.owners);
		// Anyone may subscribe to this node (and receive)
		this.setAccessModel(AccessModel.open);
		// Deliver payloads on notifications
		this.setDeliverPayloads(true);
		// Do not notify when items are deleted from a node! (I-P-Frames are deleted => subscribers should not be notified about this behaviour)
		this.setNotifyRetract(false);
		// Subscriptions are definitely allowed
		this.setSubscribe(true);
		// Subscribers should definitely be notified when configuration changed!
		this.setNotifyConfig(true);
		// Anyone may receive the items (even though he is not present)
		this.setPresenceBasedDelivery(false);
	}

}
