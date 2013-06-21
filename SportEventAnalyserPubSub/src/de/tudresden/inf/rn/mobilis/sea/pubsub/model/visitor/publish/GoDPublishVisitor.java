package de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.publish;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.SimplePayload;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticCollection;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticsFacade;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPlayerData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPositionData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentTeamData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * This <code>GoDPublishVisitor</code> (Group-of-<code>DataNode</code>
 * -PublishVisitor) is used to publish <code>Item</code>s. This
 * <code>GoDPublishVisitor</code> uses I-Frames and P-Frames to publish data.
 * The sequence of sent <code>Item</code>s consists of one I-Frame and 29
 * P-Frames per "cycle"
 */
public class GoDPublishVisitor implements Visitor {

	private PubSubManager manager;

	private StatisticsFacade statistics;

	private StatisticCollection iStatistics;

	private byte cycleCounter;

	public GoDPublishVisitor(PubSubManager manager, StatisticsFacade statistics) {
		this.manager = manager;
		this.statistics = statistics;
		this.iStatistics = new StatisticCollection();
		cycleCounter = Byte.MAX_VALUE;
	}

	private void sendPayload(String nodeName, String payload) {
		// TODO: Use publish instead! Send is definitely useful when developing
		// (due to Error-Handling on response), but "send" may take some time
		// when sending some items
		if (!payload.equals("")) {
			try {
				((LeafNode) manager.getNode(nodeName))
						.publish(new PayloadItem<SimplePayload>(
								new SimplePayload(nodeName, "pubsub:sea:"
										+ nodeName.toLowerCase(), payload)));
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void visit(StatisticCollection node) {
		if (cycleCounter > 29) {
			// Reset cycleCounter
			cycleCounter = 0;
			// Copy new statistics current content into the auxiliary
			// iStatistics (I-Frame which will no be manipulated)
			statistics.getCurrentStatistics().copy(iStatistics);
		} else {
			// Increase cycleCounter
			cycleCounter++;
		}
	}

	@Override
	public void visit(CurrentPositionData node) {
		if (cycleCounter == 0) {
			// Send I-Frame
			sendPayload(node.getNodeName(), node.toXML());
		} else {
			// Send P-Frame
			sendPayload(node.getNodeName(),
					node.toPredictiveCodedXML(iStatistics
							.getCurrentPositionData()));
		}
	}

	@Override
	public void visit(CurrentPlayerData node) {
		if (cycleCounter == 0) {
			// Send I-Frame
			sendPayload(node.getNodeName(), node.toXML());
		} else {
			// Send P-Frame
			sendPayload(node.getNodeName(),
					node.toPredictiveCodedXML(iStatistics
							.getCurrentPlayerData()));
		}
	}

	@Override
	public void visit(CurrentTeamData node) {
		if (cycleCounter == 0) {
			// Send I-Frame
			sendPayload(node.getNodeName(), node.toXML());
		} else {
			// Send P-Frame
			sendPayload(node.getNodeName(),
					node.toPredictiveCodedXML(iStatistics.getCurrentTeamData()));
		}
	}

}
