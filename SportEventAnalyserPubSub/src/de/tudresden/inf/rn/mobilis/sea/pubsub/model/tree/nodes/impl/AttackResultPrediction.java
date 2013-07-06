package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.Node;

/**
 * An <code>AttackResultPrediction</code> may be used to set the probability of
 * a specific result of an attack
 */
public class AttackResultPrediction extends Node<AttackResultPrediction> {

	/**
	 * Probability that the ball will be out of play after this attack
	 */
	private double outOfPlay;

	/**
	 * Probability that the ball turns over to the other team
	 */
	private double turnOver;

	/**
	 * Probability that this attack will end with a shot on a goal
	 */
	private double shotOnGoal;

	/**
	 * Constructor for an <code>AttackResultPrediction</code>
	 */
	public AttackResultPrediction() {
		outOfPlay = 0;
		turnOver = 0;
		shotOnGoal = 0;
	}

	/**
	 * Constructor for an <code>AttackResultPrediction</code> (which allows
	 * assigning all values directly)
	 * 
	 * @param outOfPlay
	 *            the probability that the ball will be out of play after this
	 *            attack
	 * @param turnOver
	 *            the probability that the ball turns over to the other team
	 * @param shotOnGoal
	 *            the probability that this attack will end with a shot on a
	 *            goal
	 */
	public AttackResultPrediction(double outOfPlay, double turnOver,
			double shotOnGoal) {
		this.outOfPlay = outOfPlay;
		this.turnOver = turnOver;
		this.shotOnGoal = shotOnGoal;
	}

	/**
	 * Get the probability that the ball will be out of play after this attack
	 * 
	 * @return the outOfPlay percentage
	 */
	public double getOutOfPlay() {
		return outOfPlay;
	}

	/**
	 * Set the probability that the ball will be out of play after this attack
	 * 
	 * @param outOfPlay
	 *            the outOfPlay percentage to set
	 */
	public void setOutOfPlay(double outOfPlay) {
		this.outOfPlay = outOfPlay;
	}

	/**
	 * Get the probability that the ball turns over to the other team
	 * 
	 * @return the turnOver percentage
	 */
	public double getTurnOver() {
		return turnOver;
	}

	/**
	 * Set the probability that the ball turns over to the other team
	 * 
	 * @param turnOver
	 *            the turnOver percentage to set
	 */
	public void setTurnOver(double turnOver) {
		this.turnOver = turnOver;
	}

	/**
	 * Get the probability that there will be a shot on the goal at the end of
	 * this attack
	 * 
	 * @return the shotOnGoal percentage
	 */
	public double getShotOnGoal() {
		return shotOnGoal;
	}

	/**
	 * Set the probability that there will be a shot on the goal at the end of
	 * this attack
	 * 
	 * @param shotOnGoal
	 *            the shotOnGoal percentage to set
	 */
	public void setShotOnGoal(double shotOnGoal) {
		this.shotOnGoal = shotOnGoal;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<AttackResultPrediction>");

		// outOfPlay
		sb.append("<outOfPlay>");
		sb.append(outOfPlay);
		sb.append("</outOfPlay>");

		// turnOver
		sb.append("<turnOver>");
		sb.append(turnOver);
		sb.append("</turnOver>");

		// shotOnGoal
		sb.append("<shotOnGoal>");
		sb.append(shotOnGoal);
		sb.append("</shotOnGoal>");

		sb.append("</AttackResultPrediction>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(AttackResultPrediction iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<AttackResultPrediction>");

		// outOfPlay
		if (iNode.getOutOfPlay() != outOfPlay) {
			c = true;
			sb.append("<outOfPlay>");
			sb.append(outOfPlay);
			sb.append("</outOfPlay>");
		}

		// turnOver
		if (iNode.getTurnOver() != turnOver) {
			c = true;
			sb.append("<turnOver>");
			sb.append(turnOver);
			sb.append("</turnOver>");
		}

		// shotOnGoal
		if (iNode.getShotOnGoal() != shotOnGoal) {
			c = true;
			sb.append("<shotOnGoal>");
			sb.append(shotOnGoal);
			sb.append("</shotOnGoal>");
		}

		if (c) {
			sb.append("</AttackResultPrediction>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(AttackResultPrediction dest) {
		// Copy outOfPlay
		dest.setOutOfPlay(outOfPlay);
		// Copy turnOver
		dest.setTurnOver(turnOver);
		// Copy shotOnGoal
		dest.setShotOnGoal(shotOnGoal);
	}

	@Override
	public AttackResultPrediction clone() {
		return new AttackResultPrediction(outOfPlay, turnOver, shotOnGoal);
	}

}
