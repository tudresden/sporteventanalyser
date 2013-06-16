package de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.publish;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.SimplePayload;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticCollection;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPositionData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPlayerData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentTeamData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * This <code>IntraFramePublishVisitor</code> is used to publish
 * <code>Item</code>s which holds the complete data.
 */
public class IntraFramePublishVisitor implements Visitor {

	private PubSubManager manager;

	public IntraFramePublishVisitor(PubSubManager manager) {
		this.manager = manager;
	}

	private void sendPayload(String nodeName, String payload) {
		// TODO: Use publish instead! Send is definitely useful when developing
		// (due to Error-Handling on response), but "send" may take some time
		// when sending some items
		try {
			((LeafNode) manager.getNode(nodeName))
					.send(new PayloadItem<SimplePayload>(new SimplePayload("I"
							+ nodeName,
							"pubsub:sea:i" + nodeName.toLowerCase(), payload)));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(StatisticCollection node) {
		// NOOP
	}

	@Override
	public void visit(CurrentPositionData node) {
		sendPayload(node.getNodeName(), node.toXML());
	}

	@Override
	public void visit(CurrentPlayerData node) {
		sendPayload(node.getNodeName(), node.toXML());
	}

	@Override
	public void visit(CurrentTeamData node) {
		// TODO
	}

}
