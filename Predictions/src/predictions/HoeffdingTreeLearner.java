package predictions;

import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingTree;
import moa.core.InstancesHeader;

public class HoeffdingTreeLearner extends Learner {

	public static final String TAG = "[Predictions][HoeffdingTreeLearner] ";

	Classifier learner;

	@Override
	public void init(InstancesHeader instanceHeader) {
		this.instanceHeader = instanceHeader;

		learner = new HoeffdingTree();
		learner.setModelContext(instanceHeader);
		learner.prepareForUse();
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

		learner.trainOnInstance(trainingInstance.getInstanceCopy());

		printAccuracy(TAG);
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

		printAccuracy(TAG);

	}

}
