package de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.utils.Iterator;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * A <code>TreeRunner</code> is used to iterate over a whole tree and visit all
 * <code>Node</code>s
 */
public class TreeRunner implements Runnable {

	/**
	 * A concrete <code>Visitor</code> which will be applied on each
	 * <code>Node</code>
	 */
	private Visitor visitor;

	/**
	 * An <code>Iterator</code> which is used to iterate over <code>Node</code>s
	 */
	private Iterator iterator;

	/**
	 * Constructor for a <code>TreeRunner</code>. A <code>TreeRunner</code> is
	 * used to iterate over a whole tree and visit all <code>Node</code>s
	 * 
	 * @param visitor
	 *            A concrete <code>Visitor</code> which will be applied on each
	 *            <code>Node</code>
	 * @param iterator
	 *            An <code>Iterator</code> which is used to iterate over
	 *            <code>Node</code>s
	 */
	public TreeRunner(Visitor visitor, Iterator iterator) {
		this.visitor = visitor;
		this.iterator = iterator;
	}

	@Override
	public void run() {
		while (iterator.hasNext()) {
			iterator.next().accept(visitor);
		}
	}

}
