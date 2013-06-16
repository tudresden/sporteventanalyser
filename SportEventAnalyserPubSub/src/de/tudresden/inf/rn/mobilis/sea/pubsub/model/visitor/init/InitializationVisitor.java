package de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.init;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.Node;
import org.jivesoftware.smackx.pubsub.PubSubManager;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs.CollectionConfig;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs.CommonSubmitNodeConfig;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs.NPLeafConfig;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticCollection;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPositionData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPlayerData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentTeamData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * This <code>InitializationVisitor</code> is used to initialize the node on the
 * XMPP-Server. Therefore it uses the <code>PubSubManager</code> and recreates
 * all needed <code>Node</code>s
 */
public class InitializationVisitor implements Visitor {

	private PubSubManager manager;

	public InitializationVisitor(PubSubManager manager) {
		this.manager = manager;
	}

	private Node createNode(String id, CommonSubmitNodeConfig config)
			throws XMPPException {
		Node node = null;
		try {
			node = manager.getNode(id);
		} catch (XMPPException e) {
			if (e.getXMPPError().getCode() != 404)
				throw e;
		}
		if (node != null) {
			manager.deleteNode(id);
		}
		return manager.createNode(id, config);
	}

	@Override
	public void visit(StatisticCollection statisticCollection) {
		try {
			createNode("Statistic", new CollectionConfig("StatisticCollection"));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(CurrentPositionData node) {
		try {
			createNode(node.getNodeName(), new NPLeafConfig("CurrentPositions",
					"Statistic"));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(CurrentPlayerData node) {
		try {
			createNode(node.getNodeName(), new NPLeafConfig("PlayerStatistics",
					"Statistic"));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(CurrentTeamData node) {
		try {
			createNode(node.getNodeName(), new NPLeafConfig("TeamStatistics",
					"Statistic"));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

}
