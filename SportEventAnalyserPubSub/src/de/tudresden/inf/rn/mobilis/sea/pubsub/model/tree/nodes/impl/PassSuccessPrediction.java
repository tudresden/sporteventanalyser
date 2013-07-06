package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;

/**
 * A <code>PassSuccessPrediction</code> may be used to set the probability that
 * the next pass will be successful
 */
public class PassSuccessPrediction extends Node<PassSuccessPrediction> {

	/**
	 * Probability that the next pass will be successful
	 */
	private double passSuccessful;

	/**
	 * Constructor for a <code>PassSuccessPrediction</code>
	 */
	public PassSuccessPrediction() {
		passSuccessful = 0;
	}

	/**
	 * Constructor for a <code>PassSuccessPrediction</code> (which allows assign
	 * the passSuccessful directly)
	 * 
	 * @param passSuccessful
	 *            the probability that the next pass will be successful
	 */
	public PassSuccessPrediction(double passSuccessful) {
		this.passSuccessful = passSuccessful;
	}

	/**
	 * Get the probability that the next pass will be successful
	 * 
	 * @return the passSuccessful percentage
	 */
	public double getPassSuccessful() {
		return passSuccessful;
	}

	/**
	 * Set the probability that the next pass will be successful
	 * 
	 * @param passSuccessful
	 *            the passSuccessful percentage to set
	 */
	public void setPassSuccessful(double passSuccessful) {
		this.passSuccessful = passSuccessful;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<PassSuccessPrediction>");

		// passSuccessful
		sb.append("<passSuccessful>");
		sb.append(passSuccessful);
		sb.append("</passSuccessful>");

		sb.append("</PassSuccessPrediction>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(PassSuccessPrediction iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<PassSuccessPrediction>");

		// passSuccessful
		if (iNode.getPassSuccessful() != passSuccessful) {
			c = true;
			sb.append("<passSuccessful>");
			sb.append(passSuccessful);
			sb.append("</passSuccessful>");
		}

		if (c) {
			sb.append("</PassSuccessPrediction>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(PassSuccessPrediction dest) {
		// Copy passSuccessful
		dest.setPassSuccessful(passSuccessful);
	}

	@Override
	public PassSuccessPrediction clone() {
		return new PassSuccessPrediction(passSuccessful);
	}

}
