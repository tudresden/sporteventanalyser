package de.tudresden.inf.rn.mobilis.sea.pubsub.core;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.Affiliation;
import org.jivesoftware.smackx.pubsub.PubSubManager;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.StatisticsFacade;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils.DFSIterator;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.TreeRunner;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.init.InitializationVisitor;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.publish.IntraFramePublishVisitor;

public class SportEventAnalyserPubSub {

	private Connection connection;

	private PubSubManager manager;

	private StatisticsFacade statistics;

	public SportEventAnalyserPubSub(Connection connection) {
		this.connection = connection;

		manager = new PubSubManager(connection, "pubsub."
				+ connection.getServiceName());

//		try {
//			for (Affiliation affl : manager.getAffiliations()) {
//				System.out.println(affl.getNodeId());
//			}
//		} catch (XMPPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		initializeTree();
	}

	private void initializeTree() {
		statistics = new StatisticsFacade();
		TreeRunner runner = new TreeRunner(new InitializationVisitor(manager),
				new DFSIterator(statistics.getCurrentStatistics()));
		runner.run();

		new Thread(new Runnable() {

			private long cT;

			@Override
			public void run() {
				while (true) {
					cT = System.currentTimeMillis();
					new TreeRunner(new IntraFramePublishVisitor(manager),
							new DFSIterator(statistics.getCurrentStatistics()))
							.run();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Elapsed: "
							+ (System.currentTimeMillis() - cT) + "ms");
				}
			}

		}).start();
	}

	public StatisticsFacade getStatistics() {
		return statistics;
	}
}
