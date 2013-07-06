package predictions;

import weka.core.Instances;
import moa.core.InstancesHeader;

/**
 * The Learner class encapsulates an arbitrary learner.
 * 
 */
public abstract class Learner {
	protected int numberSamplesCorrect = 0;
	protected int numberSamples = 0;
	protected InstancesHeader instanceHeader;
	protected Instances accumulatedInstances;

	protected abstract void init(InstancesHeader instanceHeader);

	/**
	 * Trains the learner.
	 * 
	 * @param trainingInstance
	 *            the instance with set type for training
	 */
	abstract void train(PredictionInstance trainingInstance);

	/**
	 * Makes a prediction without traing the learner.
	 * 
	 * @param predictionInstance
	 *            the instance to make a prediction for
	 * @return bundle of predictions sorted by class index
	 */
	abstract float[] makePrediction(PredictionInstance predictionInstance);

	/**
	 * Prints the current accuracy of prediction to console.
	 * 
	 * @param tag
	 *            string for console output
	 */
	protected void printAccuracy(String tag) {

		if (numberSamples > 0) {
			double accuracy = 100.0 * (double) numberSamplesCorrect
					/ (double) numberSamples;
			System.out.println(tag + numberSamples + " instances, " + accuracy
					+ "% accuracy");
		} else
			System.out.println(tag + numberSamples + " instances, " + "n/a"
					+ "% accuracy");
	}

	/**
	 * Returns all occurred instances. Instances are only collected if
	 * <i>Utils.ARFF_WRITING_MODE</i> is set to <i>true</i>.
	 * 
	 * @return all occurred instances
	 */
	protected Instances getAccumulatedInstances() {
		return accumulatedInstances;
	}
}
