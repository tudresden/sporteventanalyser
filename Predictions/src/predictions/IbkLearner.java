package predictions;

import moa.core.InstancesHeader;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Tag;

public class IbkLearner extends Learner {

	public static final String TAG = "[Predictions][IbkLearner] ";

	private static final int WEIGHT_SIMILARITY = 4;

	private static final int WEIGHT_NONE = 1;

	private static final int WEIGHT_INVERSE = 2;

	private static final Tag[] TAGS_WEIGHTING = {
			new Tag(4, "WEIGHT_SIMILARITY"), new Tag(1, "WEIGHT_NONE"),
			new Tag(2, "WEIGHT_INVERSE") };

	private static final int KNN = 11;

	private IBk ibk;

	private int counter = 0;

	@Override
	public void init(InstancesHeader instanceHeader) {
		this.instanceHeader = instanceHeader;

		ibk = new IBk(KNN);
		ibk.setDistanceWeighting(new SelectedTag(WEIGHT_SIMILARITY,
				TAGS_WEIGHTING));
		// int newWindowSize=300;
		// ibk.setWindowSize(newWindowSize);

		try {
			ibk.buildClassifier(new Instances(instanceHeader));
		} catch (Exception e) {
			e.printStackTrace();
		}

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

		try {
			double[] distances;

			distances = ibk.distributionForInstance(trainingInstance
					.getInstance());

			float firstClassProbability = (float) (distances[0]
					/ (distances[0] + distances[1]) * 100f);

			// prediction correct
			if (firstClassProbability > 50f && result.equals(getClassName(0))
					|| firstClassProbability <= 50f
					&& result.equals(getClassName(1))) {
				numberSamplesCorrect++;
				if (Utils.DEBUGGING)
					System.out.println(TAG + "Prediction was correct.");
			}

			// prediction wrong
			else {
				if (Utils.DEBUGGING)
					System.out.println(TAG + "Prediction was wrong.");
			}

		} catch (Exception e1) {
			if (Utils.DEBUGGING)
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
			counter++;
			if (counter > KNN)
				ibk.setCrossValidate(true);
		} catch (Exception e) {
		}

		printAccuracy(TAG);
	}

	@Override
	public void makePrediction(PredictionInstance predictionInstance) {

		try {
			double[] distances;

			distances = ibk.distributionForInstance(predictionInstance
					.getInstance());

			float firstClassProbability = (float) (distances[0]
					/ (distances[0] + distances[1]) * 100f);

			if (Utils.DEBUGGING)
				System.out.println(TAG + "prediction " + getClassName(0) + ": "
						+ firstClassProbability + "%");

		} catch (Exception e) {
			// e.printStackTrace();
			if (Utils.DEBUGGING)
				System.out.println(TAG + "no neighbors found");
		}

		printAccuracy(TAG);

	}

	private String getClassName(int index) {
		String unformatted = InstancesHeader.getClassLabelString(
				instanceHeader, index);
		String formatted = unformatted.substring(9, unformatted.length() - 1);
		return formatted;
	}

}
