package de.tudresden.inf.rn.mobilis.sea.pubsub.core;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.pubsub.PubSubManager;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticsFacade;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils.DFSIterator;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.TreeRunner;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.init.InitializationVisitor;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.publish.GoDPublishVisitor;

public class SportEventAnalyserPubSub {

	private PubSubManager manager;

	private StatisticsFacade statistics;

	public SportEventAnalyserPubSub(Connection connection) {
		manager = new PubSubManager(connection, "pubsub."
				+ connection.getServiceName());

		statistics = new StatisticsFacade();

		initializeTree();
	}

	private void initializeTree() {
		TreeRunner runner = new TreeRunner(new InitializationVisitor(manager),
				new DFSIterator(statistics));
		runner.run();

		new Thread(new Runnable() {

			private long cT;

			private DFSIterator iterator = new DFSIterator(statistics);

			private TreeRunner treeRunner = new TreeRunner(
					new GoDPublishVisitor(manager, statistics),
					iterator);

			@Override
			public void run() {
				while (true) {
					cT = System.currentTimeMillis();

					treeRunner.run();

					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//TODO: Remove this
//					System.out.println("Elapsed: "
//							+ (System.currentTimeMillis() - cT) + "ms");
				}
			}

		}).start();
	}

	public StatisticsFacade getStatistics() {
		return statistics;
	}
}
