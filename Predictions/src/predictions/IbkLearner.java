package predictions;

import moa.core.InstancesHeader;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

public class IbkLearner extends Learner {

	public static final String TAG = "[Predictions][IbkLearner] ";

	private IBk ibk;
	private InstancesHeader instanceHeader;

	public IbkLearner(InstancesHeader instanceHeader) {
		super(instanceHeader);
	}

	@Override
	public void init(InstancesHeader instanceHeader) {
		this.instanceHeader = instanceHeader;
		ibk = new IBk(3);
		try {
			ibk.buildClassifier(new Instances(instanceHeader));
		} catch (Exception e) {
			e.printStackTrace();
		}

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
				System.out.println(TAG + "Prediction was correct.");
			}

			// prediction wrong
			else {
				System.out.println(TAG + "Prediction was wrong.");
			}

		} catch (Exception e1) {
			System.out.println(TAG + "no neighbors found in "
					+ ibk.getNumTraining() + " instances");
		}

		/*
		 * train
		 */

		try {
			ibk.updateClassifier(trainingInstance.getInstanceCopy());
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

			System.out.println(TAG + "prediction " + getClassName(0) + ": "
					+ firstClassProbability + "%");

		} catch (Exception e) {
			// e.printStackTrace();
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
