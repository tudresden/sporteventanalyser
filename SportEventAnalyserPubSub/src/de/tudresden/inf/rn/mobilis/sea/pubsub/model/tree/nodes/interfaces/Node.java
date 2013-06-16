package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces;

public abstract class Node implements Cloneable {

	/**
	 * This method is used to create the corresponding xml output
	 * 
	 * @return the xml-representation of this <code>Node</code>
	 */
	public abstract String toXML();

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public abstract Node clone();
}
