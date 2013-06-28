package predictions;

import moa.core.InstancesHeader;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;

public class knnLearner extends Learner {

	public static final String TAG = "[Predictions][KnnLearner] ";

	private Instances accumulatedInstances;
	private LinearNNSearch knn;

	@Override
	public void init(InstancesHeader instanceHeader) {
		this.instanceHeader = instanceHeader;

		knn = new LinearNNSearch();
		accumulatedInstances = new Instances(instanceHeader);
	}

	@Override
	public void train(PredictionInstance trainingInstance) {
		numberSamples++;

		/*
		 * update accuracy
		 */

		String result = trainingInstance.getInstance().stringValue(
				trainingInstance.getInstance().classIndex());

		Instances nearestinstances;
		try {
			nearestinstances = knn.kNearestNeighbours(
					trainingInstance.getInstance(), 3);
			double[] distances = knn.getDistances();
			for (int neighborIndex = 0; neighborIndex < nearestinstances.size(); neighborIndex++) {
				Instance neighbor = nearestinstances.get(neighborIndex);

				// String neighborClass = neighbor.classAttribute().name();
				String neighborClass = neighbor.stringValue(neighbor
						.classIndex());

				if (Utils.DEBUGGING)
					System.out.println(TAG + (neighborIndex + 1) + ". "
							+ neighborClass + " (distance: "
							+ distances[neighborIndex] + ")");

				if (neighborIndex == 0 && result.equals(neighborClass))
					numberSamplesCorrect++;

			}

		} catch (Exception e) {
			// e.printStackTrace();
			if (Utils.DEBUGGING)
				System.out.println(TAG + "no neighbors found in "
						+ accumulatedInstances.size() + " instances");

		}

		/*
		 * train
		 */

		accumulatedInstances.add(trainingInstance.getInstanceCopy());

		try {
			knn.setInstances(accumulatedInstances);
		} catch (Exception e) {
			e.printStackTrace();
		}

		printAccuracy(TAG);
	}

	@Override
	public void makePrediction(PredictionInstance predictionInstance) {

		Instances nearestinstances;
		try {
			nearestinstances = knn.kNearestNeighbours(
					predictionInstance.getInstance(), 3);
			double[] distances = knn.getDistances();
			for (int neighborIndex = 0; neighborIndex < nearestinstances.size(); neighborIndex++) {
				Instance neighbor = nearestinstances.get(neighborIndex);
				String neighborClass = neighbor.stringValue(neighbor
						.classIndex());

				if (Utils.DEBUGGING)
					System.out.println(TAG + (neighborIndex + 1) + ". "
							+ neighborClass + " (distance: "
							+ distances[neighborIndex] + ")");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			if (Utils.DEBUGGING)
				System.out.println(TAG + "no neighbors found");
		}

		printAccuracy(TAG);

	}

}
