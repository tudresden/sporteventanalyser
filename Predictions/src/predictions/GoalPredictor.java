package predictions;

import weka.core.Instance;
import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingTree;

public class GoalPredictor {

	int numberSamplesCorrect;
	int numberSamples;
	Classifier learner;
	GoalPredictionInstanceManager instanceManager;

	public GoalPredictor() {
		init();
	}

	private void init() {
		learner = new HoeffdingTree();
		instanceManager = new GoalPredictionInstanceManager();

		learner.setModelContext(instanceManager.getHeader());
		learner.prepareForUse();

		numberSamplesCorrect = 0;
		numberSamples = 0;
	}

	/**
	 * @param event
	 *            "loss" or "out of bounds" or "goal"
	 */
	public void onEvent(String event) {
		Instance trainingInstance = instanceManager.getTrainingInstance(event);
		learner.trainOnInstance(trainingInstance);

		if (learner.correctlyClassifies(trainingInstance)) {
			numberSamplesCorrect++;
			System.out.println("[PREDICTION] prediction correct");
		} else {
			System.out.println("[PREDICTION] prediction false");
		}
		numberSamples++;
	}

	public void onPlayerChange(String nameOfPlayerWithBall) {

		// count ball possession of player
		instanceManager.updateInstance(nameOfPlayerWithBall);

		// predict
		double[] predictions = learner.getVotesForInstance(instanceManager
				.getInstanceForPrediction());

		if (predictions.length == 3) {
			double sum = predictions[0] + predictions[1] + predictions[2];
			System.out.println("[PREDICTION] " + sum + " votes:  "
					+ predictions[0] / sum * 100 + "% goal  -  "
					+ predictions[1] / sum * 100 + "% ball loss  -  "
					+ predictions[2] / sum * 100 + "% out of bounds");

		} else
			System.out.println("[PREDICTION] Votes: - n/a -");

		printAccuracy();
	}

	public void printAccuracy() {
		if (numberSamples > 0) {
			double accuracy = 100.0 * (double) numberSamplesCorrect
					/ (double) numberSamples;
			System.out.println("[PREDICTION] " + numberSamples + " instances, "
					+ accuracy + "% accuracy");
		} else
			System.out.println("[PREDICTION] " + numberSamples + " instances, "
					+ "n/a" + "% accuracy");
	}
}
