package predictions;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;
import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingTree;

public class GoalPredictor {

	int numberSamplesCorrect;
	int numberSamples;
	Classifier learner; // hoeffding tree
	LinearNNSearch knn; // k-nearest neighbor
	Instances accumulatedInstances;
	GoalPredictionInstanceManager instanceManager;

	public GoalPredictor() {
		init();
	}

	private void init() {

		numberSamplesCorrect = 0;
		numberSamples = 0;
		instanceManager = new GoalPredictionInstanceManager();

		/*
		 * hoeffding tree
		 */

		learner = new HoeffdingTree();
		learner.setModelContext(instanceManager.getHeader());
		learner.prepareForUse();

		/*
		 * KNN
		 */
		knn = new LinearNNSearch();
		accumulatedInstances = new Instances(instanceManager.getHeader());

	}

	/**
	 * @param event
	 *            "loss" or "out of bounds" or "goal"
	 */
	public void onEvent(String event) {

		Instance trainingInstance = instanceManager.getTrainingInstance(event);

		/*
		 * Hoeffding tree
		 */

		// String predictedEvent = "n/a";
		// double[] votes = learner.getVotesForInstance(trainingInstance);
		//
		// if (votes.length == 3) {
		// if (votes[0] > votes[1] && votes[0] > votes[2])
		// predictedEvent = GoalPredictionInstanceManager.EVENT_GOAL;
		// else if (votes[1] > votes[0] && votes[1] > votes[2])
		// predictedEvent = GoalPredictionInstanceManager.EVENT_BALL_LOSS;
		// else if (votes[2] > votes[0] && votes[2] > votes[1])
		// predictedEvent =
		// GoalPredictionInstanceManager.EVENT_BALL_OUT_OF_BOUNDS;
		// }
		//
		// if (learner.correctlyClassifies(trainingInstance)) {
		// numberSamplesCorrect++;
		// System.out.println("[PREDICTION] prediction correct: "
		// + predictedEvent);
		// } else {
		// System.out.println("[PREDICTION] prediction false: "
		// + predictedEvent);
		// }
		// numberSamples++;
		//
		// // train
		// learner.trainOnInstance(trainingInstance);

		/*
		 * KNN
		 */

		Instances nearestinstances;
		try {
			nearestinstances = knn.kNearestNeighbours(trainingInstance, 3);
			double[] distances = knn.getDistances();
			for (int neighborIndex = 0; neighborIndex < nearestinstances.size(); neighborIndex++) {
				Instance neighbor = nearestinstances.get(neighborIndex);
				
//				String neighborClass = neighbor.classAttribute().name();
				String neighborClass = neighbor.stringValue(neighbor.classIndex());
				
				System.out.println("[PREDICTION] " + (neighborIndex + 1) + ". "
						+ neighborClass + " (distance: "
						+ distances[neighborIndex] + ")");

				if (neighborIndex == 0 && event.equals(neighborClass))
					numberSamplesCorrect++;

			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("[PREDICTION] no neighbors found in "
					+ accumulatedInstances.size() + " instances");

		}

		// train
		accumulatedInstances.add(trainingInstance);
		try {
			knn.setInstances(accumulatedInstances);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * output
		 */

		System.out.println("[PREDICTION] real event: " + event);
		numberSamples++;
	}

	public void onPlayerChange(String nameOfPlayerWithBall) {

		// count ball possession of player
		instanceManager.updateInstance(nameOfPlayerWithBall);

		/*
		 * Hoeffding tree
		 */

		// predict
		double[] predictions = learner.getVotesForInstance(instanceManager
				.getInstanceForPrediction());

		if (predictions.length == 3) {
			double sum = predictions[0] + predictions[1] + predictions[2];
			System.out.println("[PREDICTION] " + sum + " votes:  "
					+ predictions[0] / sum * 100 + "% goal  -  "
					+ predictions[1] / sum * 100 + "% ball loss  -  "
					+ predictions[2] / sum * 100 + "% out of bounds");

			String predictedEvent = "n/a";
			if (predictions[0] > predictions[1]
					&& predictions[0] > predictions[2])
				predictedEvent = GoalPredictionInstanceManager.EVENT_GOAL;
			else if (predictions[1] > predictions[0]
					&& predictions[1] > predictions[2])
				predictedEvent = GoalPredictionInstanceManager.EVENT_BALL_LOSS;
			else if (predictions[2] > predictions[0]
					&& predictions[2] > predictions[1])
				predictedEvent = GoalPredictionInstanceManager.EVENT_BALL_OUT_OF_BOUNDS;
			System.out.println("[PREDICTION] predicted event: "
					+ predictedEvent);

		} else
			System.out.println("[PREDICTION] Votes: - n/a -");

		/*
		 * KNN
		 */

		Instances nearestinstances;
		try {
			nearestinstances = knn.kNearestNeighbours(
					instanceManager.getInstanceForPrediction(), 3);
			double[] distances = knn.getDistances();
			for (int neighborIndex = 0; neighborIndex < nearestinstances.size(); neighborIndex++) {
				Instance neighbor = nearestinstances.get(neighborIndex);

//				String neighborClass = neighbor.classAttribute().name();
//				String neighborClass = neighbor.stringValue((int) neighbor.classValue());
				String neighborClass = neighbor.stringValue(neighbor.classIndex());

				System.out.println("[PREDICTION] " + (neighborIndex + 1) + ". "
						+ neighborClass + " (distance: "
						+ distances[neighborIndex] + ")");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("[PREDICTION] no neighbors found");
		}

		/*
		 * output
		 */

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
