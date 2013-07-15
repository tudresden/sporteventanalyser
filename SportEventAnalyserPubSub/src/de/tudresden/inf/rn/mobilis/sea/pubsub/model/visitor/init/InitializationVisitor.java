package de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.init;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.Node;
import org.jivesoftware.smackx.pubsub.PubSubManager;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs.CollectionConfig;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs.CommonSubmitNodeConfig;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.configs.NPLeafConfig;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticCollection;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentGameData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentHeatMapData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPositionData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPlayerData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentPrognosisData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl.CurrentTeamData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * This <code>InitializationVisitor</code> is used to initialize the node on the
 * XMPP-Server. Therefore it uses the <code>PubSubManager</code> and recreates
 * all needed <code>Node</code>s
 */
public class InitializationVisitor implements Visitor {

	/**
	 * This is the starting point for access to the pubsub service. It provides
	 * access to general information about the service, as well as create or
	 * retrieve pubsub LeafNode instances. These instances provide the bulk of
	 * the functionality as defined in the pubsub specification XEP-0060
	 */
	private PubSubManager manager;

	/**
	 * Constructor for an <code>InitializationVisitor</code>
	 * 
	 * @param manager
	 *            the <code>smackx.pubsub.PubSubManager</code> to access the
	 *            pubsub service
	 */
	public InitializationVisitor(PubSubManager manager) {
		this.manager = manager;
	}

	/**
	 * Create a PubSub-<code>Node</code>. If there is already a
	 * <code>Node</code> with such an id, this <code>Node</code> will be deleted
	 * first and then replaced with a new <code>Node</code>. This ensures that
	 * there are no more old items which may corrupt the data
	 * 
	 * @param id
	 *            the ID of the <code>Node</code> which should be added
	 * @param config
	 *            a <code>CommonSubmitNodeConfig</code> to be used with the
	 *            <code>Node</code>
	 * @return the created <code>Node</code>
	 * @throws XMPPException
	 */
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

	@Override
	public void visit(CurrentHeatMapData node) {
		try {
			createNode(node.getNodeName(), new NPLeafConfig(
					"HeatMapStatistics", "Statistic"));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(CurrentPrognosisData node) {
		try {
			createNode(node.getNodeName(), new NPLeafConfig("Prognosis",
					"Statistic"));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(CurrentGameData node) {
		try {
			createNode(node.getNodeName(), new NPLeafConfig("GameData",
					"Statistic"));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

}
