package de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.publish;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.SimplePayload;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticCollection;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticsFacade;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentGameData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentHeatMapData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPlayerData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPositionData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPrognosisData;
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

	/**
	 * This is the starting point for access to the pubsub service. It provides
	 * access to general information about the service, as well as create or
	 * retrieve pubsub LeafNode instances. These instances provide the bulk of
	 * the functionality as defined in the pubsub specification XEP-0060
	 */
	private PubSubManager manager;

	/**
	 * The <code>StatisticsFacade</code> to publish items
	 */
	private StatisticsFacade statistics;

	/**
	 * An internal model which will be refilled with the current data on each
	 * sent intra-frame (it is used to get the difference between a predicitve
	 * coded frame and intra-frames)
	 */
	private StatisticCollection iStatistics;

	/**
	 * Counts the cycles (is resettet when an intra-frame has been sent)
	 */
	private byte cycleCounter;

	/**
	 * Constructor for a <code>GoDPublishVisitor</code>
	 * 
	 * @param manager
	 *            the <code>smackx.pubsub.PubSubManager</code> to access the
	 *            pubsub service
	 * @param statistics
	 *            the <code>StatisticsFacade</code> to publish items
	 */
	public GoDPublishVisitor(PubSubManager manager, StatisticsFacade statistics) {
		this.manager = manager;
		this.statistics = statistics;
		this.iStatistics = new StatisticCollection();
		cycleCounter = Byte.MAX_VALUE;
	}

	/**
	 * Sends the given payload to the node with the given name. If the payload
	 * is empty nothing will be published
	 * 
	 * @param nodeName
	 *            the name of the <code>Node</code> where the data should be
	 *            published
	 * @param payload
	 *            the payload which should be sent
	 */
	private void sendPayload(String nodeName, String payload) {
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

	@Override
	public void visit(CurrentHeatMapData node) {
		// Just send HeatMap-Updates with I-Frames
		if (cycleCounter == 0) {
			sendPayload(node.getNodeName(), node.toXML());
		}
	}

	@Override
	public void visit(CurrentPrognosisData node) {
		if (cycleCounter == 0) {
			// Send I-Frame
			sendPayload(node.getNodeName(), node.toXML());
		} else {
			// Send P-Frame
			sendPayload(node.getNodeName(),
					node.toPredictiveCodedXML(iStatistics
							.getCurrentPrognosisData()));
		}
	}

	@Override
	public void visit(CurrentGameData node) {
		// Just send P-Frames (currently only the playingTime is sent)
		if (cycleCounter > 0) {
			// Send P-Frame
			sendPayload(node.getNodeName(),
					node.toPredictiveCodedXML(iStatistics.getCurrentGameData()));
		}
	}

}
