package predictions;

import moa.core.InstancesHeader;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;

public class knnLearner extends Learner {

	public static final String TAG = "[Predictions][knnLearner] ";

	private Instances accumulatedInstances;
	private LinearNNSearch knn;

	private int numberSamplesCorrect = 0;
	private int numberSamples = 0;

	public knnLearner(InstancesHeader instanceHeader) {
		super(instanceHeader);
	}

	@Override
	public void init(InstancesHeader instanceHeader) {
		knn = new LinearNNSearch();
		accumulatedInstances = new Instances(instanceHeader);

	}

	@Override
	public void train(PredictionInstance trainingInstance) {
		numberSamples++;

		/*
		 * update accuracy
		 */

		String result = trainingInstance.getInstanceForPrediction()
				.stringValue(
						trainingInstance.getInstanceForPrediction()
								.classIndex());

		Instances nearestinstances;
		try {
			nearestinstances = knn.kNearestNeighbours(
					trainingInstance.getInstanceForPrediction(), 3);
			double[] distances = knn.getDistances();
			for (int neighborIndex = 0; neighborIndex < nearestinstances.size(); neighborIndex++) {
				Instance neighbor = nearestinstances.get(neighborIndex);

				// String neighborClass = neighbor.classAttribute().name();
				String neighborClass = neighbor.stringValue(neighbor
						.classIndex());

				System.out.println(TAG + (neighborIndex + 1) + ". "
						+ neighborClass + " (distance: "
						+ distances[neighborIndex] + ")");

				if (neighborIndex == 0 && result.equals(neighborClass))
					numberSamplesCorrect++;

			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(TAG + "no neighbors found in "
					+ accumulatedInstances.size() + " instances");

		}

		/*
		 * train
		 */

		accumulatedInstances.add(trainingInstance.getInstanceForTraining());

		try {
			knn.setInstances(accumulatedInstances);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public int getPrediction(PredictionInstance predictionInstance) {

		Instances nearestinstances;
		try {
			nearestinstances = knn.kNearestNeighbours(
					predictionInstance.getInstanceForPrediction(), 3);
			double[] distances = knn.getDistances();
			for (int neighborIndex = 0; neighborIndex < nearestinstances.size(); neighborIndex++) {
				Instance neighbor = nearestinstances.get(neighborIndex);
				String neighborClass = neighbor.stringValue(neighbor
						.classIndex());

				System.out.println(TAG + (neighborIndex + 1) + ". "
						+ neighborClass + " (distance: "
						+ distances[neighborIndex] + ")");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(TAG + "no neighbors found");
		}

		printAccuracy();

		return 0;
	}

	public void printAccuracy() {
		if (numberSamples > 0) {
			double accuracy = 100.0 * (double) numberSamplesCorrect
					/ (double) numberSamples;
			System.out.println(TAG + numberSamples + " instances, "
					+ accuracy + "% accuracy");
		} else
			System.out.println(TAG + numberSamples + " instances, "
					+ "n/a" + "% accuracy");
	}

}
