package de.tudresden.inf.rn.mobilis.sea.pubsub.core;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.pubsub.PubSubManager;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticsFacade;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils.DFSIterator;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.TreeRunner;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.impl.GoDPublishVisitor;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.impl.InitializationVisitor;

/**
 * This <code>SportEventAnalyserPubSub</code> is used to handle the bulk of
 * functionality which comes along with XEP-0060 (PubSub). Therefore it uses an
 * internal model which is accessed by using the <code>StatisticsFacade</code>
 * and may be filled with new data. Any changes within the model are published
 * frequently so any client may subscribe itself within the running game and
 * still has the ability to get all current informations
 */
public class SportEventAnalyserPubSub {

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
	 * Constructor for this <code>SportEventAnalyserPubSub</code>
	 * 
	 * @param connection
	 *            the <code>smack.Connection</code> which is used to communicate
	 *            with the XMPP-Server
	 */
	public SportEventAnalyserPubSub(Connection connection) {
		manager = new PubSubManager(connection, "pubsub."
				+ connection.getServiceName());

		statistics = new StatisticsFacade();

		initializeTree();
	}

	/**
	 * Initializes the PubSub-Tree on the XMPP-Server and starts a
	 * <code>Thread</code> which will publish new items in a 30ms cycle (~30Hz)
	 */
	private void initializeTree() {
		TreeRunner runner = new TreeRunner(new InitializationVisitor(manager),
				new DFSIterator(statistics));
		runner.run();

		new Thread(new Runnable() {

			private DFSIterator iterator = new DFSIterator(statistics);

			private TreeRunner treeRunner = new TreeRunner(
					new GoDPublishVisitor(manager, statistics), iterator);

			@Override
			public void run() {
				while (true) {
					treeRunner.run();

					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}

	/**
	 * Get the <code>StatisticsFacade</code> which should be used to publish new
	 * items
	 * 
	 * @return the <code>StatisticsFacade</code> to publish items
	 */
	public StatisticsFacade getStatistics() {
		return statistics;
	}
}
