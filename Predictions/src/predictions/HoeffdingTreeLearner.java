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

	public static final String TAG = "[Predictions][HoeffdingTreeLearner] ";

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
			if (Utils.DEBUGGING)
				System.out.println(TAG + "Prediction was correct.");
		} else {
			if (Utils.DEBUGGING)
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
	public void makePrediction(PredictionInstance predictionInstance) {

		double[] predictions = learner.getVotesForInstance(predictionInstance
				.getInstance());

		if (predictions.length == 2) {
			double sum = predictions[0] + predictions[1];
			if (Utils.DEBUGGING)
				System.out.println(TAG + " prediction:  " + predictions[0]
						/ sum * 100 + "% pass will be successful");

		} else if (Utils.DEBUGGING)
			System.out.println("[PREDICTION] Votes: - n/a -");

		if (Utils.DEBUGGING)
			printAccuracy(TAG
					+ InstancesHeader.getClassNameString(instanceHeader) + " ");

	}

}
