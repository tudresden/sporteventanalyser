package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces;

/**
 * Any nodes within this PubSub-model are concrete <code>Node</code>s. A
 * <code>Node</code> implements the <code>Cloneable</code> interface to indicate
 * to the <code>java.lang.Object.clone()</code> method that it is legal for that
 * method to make a field-for-field copy of instances of that class. Therefore a
 * <code>Node</code> should pass a generic parameter to declare the type of the
 * clone
 * 
 * @param <T>
 *            generic parameter to declare the type of this node
 */
public abstract class Node<T> implements Cloneable {

	/**
	 * This method is used to create the corresponding xml output (on I-Frames)
	 * 
	 * @return the xml-representation of this <code>Node</code>
	 */
	public abstract String toXML();

	/**
	 * This method is used to create the corresponding predictive-coded xml
	 * output (on P-Frames). The data is not encoded like it is by P-Frames in
	 * pictures. The original <code>Node</code> will only be compared to this
	 * <code>Node</code> and all changed fields are written into the xml
	 * <code>String</code> (not the difference between the original value and
	 * new value!)
	 * 
	 * @param iNode
	 *            the I-Frame <code>Node</code> which is used to create the
	 *            P-Frame output
	 * @return the predictive-coded xml-representation of this <code>Node</code>
	 *         or an empty <code>String</code> if data is equal
	 */
	public abstract String toPredictiveCodedXML(T iNode);

	/**
	 * Copy this <code>Node</code>'s content to the targeted <code>Node</code>
	 * 
	 * @param dest
	 *            the destination where to copy the content to
	 */
	public abstract void copy(T dest);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public abstract T clone();

}
