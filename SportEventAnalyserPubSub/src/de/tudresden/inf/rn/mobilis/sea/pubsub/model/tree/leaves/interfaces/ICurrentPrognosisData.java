package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.interfaces;

/**
 * This is an auxiliary <code>interface</code> to declare some methods which may
 * be done on a <code>CurrentPrognosisData</code> <code>Node</code>
 */
public interface ICurrentPrognosisData {

	/**
	 * Set the probability that the next pass will be successful
	 * 
	 * @param passSuccessful
	 *            the probability that the next pass will be successful
	 */
	public void setPassSuccessPrediction(double passSuccessful);

	/**
	 * Set the probability that an attack will result with a specific result
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
	public void setAttackResultPrediction(double outOfPlay, double turnOver,
			double shotOnGoal);
}
