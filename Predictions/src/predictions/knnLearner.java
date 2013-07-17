package predictions;

import moa.core.InstancesHeader;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;

/**
 * This class encapsulates the <i>LinearNNSearch</i> classifier.
 * 
 */
public class knnLearner extends Learner {
	/**
	 * Tag for logs.
	 */
	public static final String TAG = "[Predictions][KnnLearner] ";

	/**
	 * The classifier used for training and predictions.
	 */
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

				if (Utils.WRITE_DEBUGGING_LOGS)
					System.out.println(TAG + (neighborIndex + 1) + ". "
							+ neighborClass + " (distance: "
							+ distances[neighborIndex] + ")");

				if (neighborIndex == 0 && result.equals(neighborClass))
					numberSamplesCorrect++;

			}

		} catch (Exception e) {
			// e.printStackTrace();
			if (Utils.WRITE_DEBUGGING_LOGS)
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

		printAccuracy(TAG + InstancesHeader.getClassNameString(instanceHeader)
				+ " ");
	}

	@Override
	public float[] makePrediction(PredictionInstance predictionInstance) {

		float[] predictionsBundle = new float[3];

		Instances nearestinstances;
		try {
			nearestinstances = knn.kNearestNeighbours(
					predictionInstance.getInstance(), 3);
			double[] distances = knn.getDistances();
			for (int neighborIndex = 0; neighborIndex < nearestinstances.size(); neighborIndex++) {
				Instance neighbor = nearestinstances.get(neighborIndex);
				String neighborClass = neighbor.stringValue(neighbor
						.classIndex());

				if (Utils.WRITE_DEBUGGING_LOGS)
					System.out.println(TAG + (neighborIndex + 1) + ". "
							+ neighborClass + " (distance: "
							+ distances[neighborIndex] + ")");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			if (Utils.WRITE_DEBUGGING_LOGS)
				System.out.println(TAG + "no neighbors found");
		}

		if (Utils.WRITE_DEBUGGING_LOGS)
			printAccuracy(TAG
					+ InstancesHeader.getClassNameString(instanceHeader) + " ");

		return predictionsBundle;
	}

}
