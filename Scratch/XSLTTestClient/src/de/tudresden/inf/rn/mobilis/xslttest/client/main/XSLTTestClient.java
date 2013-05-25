package de.tudresden.inf.rn.mobilis.xslttest.client.main;

import java.util.LinkedList;
import java.util.List;

import de.tudresden.inf.rn.mobilis.xslttest.client.connection.MobilisConnectionManager;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.IXMPPCallback;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.InComplexXSSequence;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.OutElementXSSequence;
import de.tudresden.inf.rn.mobilis.xslttest.client.proxy.XSLTTestProxy;

public class XSLTTestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				Object o = new Object();
				synchronized (o) {
					try {
						o.wait();
					} catch (InterruptedException e) {
						// Ignore (just --keep-alive)
					}
				}
			}
		}.start();
		
		MobilisConnectionManager manager = new MobilisConnectionManager();
		manager.connect("127.0.0.1", 5222, "localhost");
		manager.performLogin("seaclient", "sea", "SEAClient",
				"mobilis@sea/XSLTTest");

		XSLTTestProxy proxy = new XSLTTestProxy(manager);

		// Drive some tests

		// Some sequences
		LinkedList<Long> inSeq = new LinkedList<Long>();
		inSeq.add(0l);
		inSeq.add(0l);
		inSeq.add(7l);
		
		LinkedList<Long> inAnotherSeq = new LinkedList<Long>();
		inAnotherSeq.add(Long.MIN_VALUE);
		inAnotherSeq.add(0l);
		inAnotherSeq.add(Long.MAX_VALUE);

		// InOnlyOperation
		proxy.InOnlyOperation("mobilis@sea/XSLTTest");

		// InOnlyOperationWithFault
		List<InComplexXSSequence> inComplexSeq = new LinkedList<InComplexXSSequence>();
		inComplexSeq.add(new InComplexXSSequence(inSeq));
		inComplexSeq.add(new InComplexXSSequence(inAnotherSeq));
		proxy.InOnlyOperationWithFault("mobilis@sea/XSLTTest", 0, 1l, false,
				2.0d, 1.23f, (byte) Byte.MAX_VALUE, (short) Short.MAX_VALUE,
				"Hey", inComplexSeq);

		
		proxy.InOutOperationWithFault("mobilis@sea/XSLTTest", inSeq, new IXMPPCallback<OutElementXSSequence>() {

			@Override
			public void invoke(OutElementXSSequence out) {
				System.out.println(out.toXML());
			}
			
		});
	}

}
