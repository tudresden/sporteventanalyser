package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.impl;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.AttackResultPrediction;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PassSuccessPrediction;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.interfaces.DataNode;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.visitor.interfaces.Visitor;

/**
 * This <code>CurrentPrognosisData</code> is a concrete <code>DataNode</code>.
 * It holds all data which is predicted
 */
public class CurrentPrognosisData extends DataNode<CurrentPrognosisData> {

	/**
	 * The name of this node
	 */
	private static final String NODENAME = "CurrentPrognosisData";

	/**
	 * A <code>PassSuccessPrediction</code> <code>Node</code> to the set
	 * probability that the next pass is successful
	 */
	private PassSuccessPrediction passSuccessPrediction;

	/**
	 * A <code>AttackResultPrediction</code> <code>Node</code> to the set
	 * probability of a specific result of an attack
	 */
	private AttackResultPrediction attackResultPrediction;

	/**
	 * Constructor for a <code>CurrentPrognosisData</code>
	 */
	public CurrentPrognosisData() {
		passSuccessPrediction = new PassSuccessPrediction();
		attackResultPrediction = new AttackResultPrediction();
	}

	/**
	 * Private <code>CurrentPrognosisData</code> constructor (for cloning
	 * purposes)
	 * 
	 * @param passSuccessPrediction
	 *            the original <code>PassSuccessPrediction</code>
	 * @param attackResultPrediction
	 *            the original <code>AttackResultPrediction</code>
	 */
	private CurrentPrognosisData(PassSuccessPrediction passSuccessPrediction,
			AttackResultPrediction attackResultPrediction) {
		this.passSuccessPrediction = passSuccessPrediction;
		this.attackResultPrediction = attackResultPrediction;
	}

	/**
	 * Get the <code>PassSuccessPrediction</code> <code>Node</code> to the set
	 * probability that the next pass is successful
	 * 
	 * @return the passSuccessPrediction <code>Node</code>
	 */
	public PassSuccessPrediction getPassSuccessPrediction() {
		return passSuccessPrediction;
	}

	/**
	 * Get the <code>AttackResultPrediction</code> <code>Node</code> to the set
	 * probability of a specific result of an attack
	 * 
	 * @return the attackResultPrediction <code>Node</code>
	 */
	public AttackResultPrediction getAttackResultPrediction() {
		return attackResultPrediction;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String getNodeName() {
		return NODENAME;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<CurrentPrognosisData>");

		// Append passSuccessPrediction
		sb.append(passSuccessPrediction.toXML());

		// Append attackResultPrediction
		sb.append(attackResultPrediction.toXML());

		sb.append("</CurrentPrognosisData>");

		return sb.toString();
	}

	@Override
	public String toPredictiveCodedXML(CurrentPrognosisData iNode) {
		boolean c = false;
		StringBuilder sb = new StringBuilder();

		sb.append("<CurrentPrognosisData>");

		// Append passSuccessPrediction
		String s = passSuccessPrediction.toPredictiveCodedXML(iNode
				.getPassSuccessPrediction());
		if (s.length() > 0) {
			c = true;
			sb.append(s);
		}

		// Append attackResultPrediction
		s = attackResultPrediction.toPredictiveCodedXML(iNode
				.getAttackResultPrediction());
		if (s.length() > 0) {
			c = true;
			sb.append(s);
		}

		if (c) {
			sb.append("</CurrentPrognosisData>");

			return sb.toString();
		}

		return "";
	}

	@Override
	public void copy(CurrentPrognosisData dest) {
		// Copy passSuccessPrediciton
		passSuccessPrediction.copy(dest.getPassSuccessPrediction());
		// Copy attackResultPrediction
		attackResultPrediction.copy(dest.getAttackResultPrediction());
	}

	@Override
	public CurrentPrognosisData clone() {
		return new CurrentPrognosisData(passSuccessPrediction.clone(),
				attackResultPrediction.clone());
	}

}
