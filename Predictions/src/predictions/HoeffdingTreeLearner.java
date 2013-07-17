package predictions;

import weka.core.Instances;
import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingTree;
import moa.core.InstancesHeader;

/**
 * This class encapsulates the <i>HoeffdingTree</i> classifier.
 * 
 */
public class HoeffdingTreeLearner extends Learner {

	/**
	 * Tag for logs.
	 */
	public static final String TAG = "[Predictions][HoeffdingTreeLearner] ";

	/**
	 * The classifier used for training and predictions.
	 */
	Classifier learner;

	@Override
	public void init(InstancesHeader instanceHeader) {
		this.instanceHeader = instanceHeader;

		learner = new HoeffdingTree();
		learner.setModelContext(instanceHeader);
		learner.prepareForUse();

		accumulatedInstances = new Instances(instanceHeader);

	}

	@Override
	public void train(PredictionInstance trainingInstance) {
		numberSamples++;

		/*
		 * update accuracy
		 */

		// String result = trainingInstance.getInstance().stringValue(
		// trainingInstance.getInstance().classIndex());

		if (learner.correctlyClassifies(trainingInstance.getInstance())) {
			numberSamplesCorrect++;
			if (Utils.WRITE_DEBUGGING_LOGS)
				System.out.println(TAG + "Prediction was correct.");
		} else {
			if (Utils.WRITE_DEBUGGING_LOGS)
				System.out.println(TAG + "Prediction was wrong.");
		}

		/*
		 * train
		 */

		if (Utils.ARFF_WRITING_MODE)
			accumulatedInstances.add(trainingInstance.getInstanceCopy());

		learner.trainOnInstance(trainingInstance.getInstanceCopy());

		printAccuracy(TAG + InstancesHeader.getClassNameString(instanceHeader)
				+ " ");
	}

	@Override
	public float[] makePrediction(PredictionInstance predictionInstance) {

		float[] predictionsBundle = new float[3];

		double[] predictions = learner.getVotesForInstance(predictionInstance
				.getInstance());

		if (predictions.length == 2) {
			double sum = predictions[0] + predictions[1];
			predictionsBundle[0] = (float) (predictions[0] / sum * 100f);
			predictionsBundle[1] = 100f - predictionsBundle[0];

		} else if (predictions.length == 3) {
			double sum = predictions[0] + predictions[1] + predictions[2];
			predictionsBundle[0] = (float) (predictions[0] / sum * 100f);
			predictionsBundle[1] = (float) (predictions[1] / sum * 100f);
			predictionsBundle[2] = 100f - predictionsBundle[0]
					- predictionsBundle[1];
		}

		if (Utils.WRITE_DEBUGGING_LOGS)
			printAccuracy(TAG
					+ InstancesHeader.getClassNameString(instanceHeader) + " ");

		return predictionsBundle;
	}

}
