package predictions;

import moa.core.InstancesHeader;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

/**
 * This class encapsulates the <i>IBk</i> classifier.
 * 
 */
public class IbkLearner extends Learner {
	/**
	 * Tag for logs.
	 */
	public static final String TAG = "[Predictions][IbkLearner] ";

	// private static final int WEIGHT_SIMILARITY = 4;
	//
	// private static final int WEIGHT_NONE = 1;
	//
	// private static final int WEIGHT_INVERSE = 2;

	/**
	 * Specifies if the knn value will be adjusted automatically by cross
	 * validation.
	 */
	private boolean adaptiveKNN;

	// private static final Tag[] TAGS_WEIGHTING = {
	// new Tag(4, "WEIGHT_SIMILARITY"), new Tag(1, "WEIGHT_NONE"),
	// new Tag(2, "WEIGHT_INVERSE") };

	/**
	 * The number of nearest neighbors used for prediction. Will be adjusted
	 * automatically.
	 */
	private int knn = 10;

	/**
	 * The classifier used for training and predictions.
	 */
	private IBk ibk;

	/**
	 * Initiates an IBk learner instance with adaptive knn value.
	 */
	public IbkLearner() {
		adaptiveKNN = true;
	}

	/**
	 * Initiates an IBk learner instance with chosen knn value.
	 * 
	 * @param knn
	 *            number of nearest neighbors used for prediction
	 */
	public IbkLearner(int knn) {
		this.knn = knn;
		adaptiveKNN = false;
	}

	@Override
	public void init(InstancesHeader instanceHeader) {
		this.instanceHeader = instanceHeader;

		ibk = new IBk(knn);

		// try {
		// ibk.setOptions(new String[] {
		// "-K",
		// "1",
		// "-W",
		// "0",
		// "-A",
		// "weka.core.neighboursearch.LinearNNSearch -A \"weka.core.ChebyshevDistance -R first-last\""
		// }); //
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		//

		// ibk.setDistanceWeighting(new SelectedTag(WEIGHT_SIMILARITY,
		// TAGS_WEIGHTING));
		// int newWindowSize = 300;
		// ibk.setWindowSize(newWindowSize);

		// LinearNNSearch linearNNSearch= new LinearNNSearch();
		// try {
		// ChebyshevDistance chebyshevDistance = new ChebyshevDistance();
		// String[] options = {"-R first-last"};
		// chebyshevDistance.setOptions(options);
		// linearNNSearch.setDistanceFunction(new ChebyshevDistance());
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		// ibk.setNearestNeighbourSearchAlgorithm(linearNNSearch);

		try {
			ibk.buildClassifier(new Instances(instanceHeader));
		} catch (Exception e) {
			e.printStackTrace();
		}

		accumulatedInstances = new Instances(instanceHeader);

	}

	@Override
	public void train(PredictionInstance trainingInstance) {

		if (Utils.WRITE_DEBUGGING_LOGS)
			System.out.println("KNN = " + ibk.getKNN());

		numberSamples++;

		/*
		 * update accuracy
		 */

		String result = trainingInstance.getInstance().stringValue(
				trainingInstance.getInstance().classIndex());

		try {
			double[] distances;

			distances = ibk.distributionForInstance(trainingInstance
					.getInstance());

			if (distances.length == 2) {
				float firstClassProbability = (float) (distances[0]
						/ (distances[0] + distances[1]) * 100f);

				// prediction correct
				if (firstClassProbability >= 50f
						&& result.equals(getClassName(0))
						|| firstClassProbability < 50f
						&& result.equals(getClassName(1))) {
					numberSamplesCorrect++;
					if (Utils.WRITE_DEBUGGING_LOGS)
						System.out.println(TAG + "Prediction was correct.");
				}

				// prediction wrong
				else {
					if (Utils.WRITE_DEBUGGING_LOGS)
						System.out.println(TAG + "Prediction was wrong.");
				}
			} else {
				float firstClassProbability = (float) (distances[0]
						/ (distances[0] + distances[1] + distances[2]) * 100f);
				float secondClassProbability = (float) (distances[1]
						/ (distances[0] + distances[1] + distances[2]) * 100f);
				float thirdClassProbability = 100 - firstClassProbability
						- secondClassProbability;

				// prediction correct
				if (firstClassProbability >= secondClassProbability
						&& firstClassProbability >= thirdClassProbability
						&& result.equals(getClassName(0))
						|| secondClassProbability >= firstClassProbability
						&& secondClassProbability > thirdClassProbability
						&& result.equals(getClassName(1))
						|| thirdClassProbability > secondClassProbability
						&& thirdClassProbability > firstClassProbability
						&& result.equals(getClassName(2))) {
					numberSamplesCorrect++;
					if (Utils.WRITE_DEBUGGING_LOGS)
						System.out.println(TAG + "Prediction was correct.");
				}

				// prediction wrong
				else {
					if (Utils.WRITE_DEBUGGING_LOGS)
						System.out.println(TAG + "Prediction was wrong.");
				}
			}

		} catch (Exception e1) {
			if (Utils.WRITE_DEBUGGING_LOGS)
				System.out.println(TAG + "no neighbors found in "
						+ ibk.getNumTraining() + " instances");
		}

		/*
		 * train
		 */

		if (Utils.ARFF_WRITING_MODE)
			accumulatedInstances.add(trainingInstance.getInstanceCopy());

		try {
			ibk.updateClassifier(trainingInstance.getInstanceCopy());
			if (ibk.getNumTraining() > knn && adaptiveKNN)
				ibk.setCrossValidate(true);
		} catch (Exception e) {
		}

		printAccuracy(TAG + InstancesHeader.getClassNameString(instanceHeader)
				+ " ");
	}

	@Override
	public float[] makePrediction(PredictionInstance predictionInstance) {

		float[] predictionsBundle = new float[3];

		try {
			double[] distances;

			distances = ibk.distributionForInstance(predictionInstance
					.getInstance());

			if (distances.length == 2) {
				float firstClassProbability = (float) (distances[0]
						/ (distances[0] + distances[1]) * 100f);

				predictionsBundle[0] = firstClassProbability;
				predictionsBundle[1] = 100f - firstClassProbability;

			} else if (distances.length == 3) {
				float firstClassProbability = (float) (distances[0]
						/ (distances[0] + distances[1] + distances[2]) * 100f);
				float secondClassProbability = (float) (distances[1]
						/ (distances[0] + distances[1] + distances[2]) * 100f);
				float thirdClassProbability = 100 - firstClassProbability
						- secondClassProbability;

				predictionsBundle[0] = firstClassProbability;
				predictionsBundle[1] = secondClassProbability;
				predictionsBundle[2] = thirdClassProbability;
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

	/**
	 * Cleans the class label.
	 * 
	 * @param index
	 *            the index of the class
	 * @return clean class label
	 */
	private String getClassName(int index) {
		String unformatted = InstancesHeader.getClassLabelString(
				instanceHeader, index);
		String formatted = unformatted.substring(9, unformatted.length() - 1);
		return formatted;
	}

}
