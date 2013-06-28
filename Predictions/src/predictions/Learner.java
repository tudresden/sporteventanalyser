package predictions;

import weka.core.Instances;
import moa.core.InstancesHeader;

public abstract class Learner {
	protected int numberSamplesCorrect = 0;
	protected int numberSamples = 0;
	protected InstancesHeader instanceHeader;
	protected Instances accumulatedInstances;

	protected abstract void init(InstancesHeader instanceHeader);

	abstract void train(PredictionInstance trainingInstance);

	abstract void makePrediction(PredictionInstance predictionInstance);

	protected void printAccuracy(String tag) {

		if (numberSamples > 0) {
			double accuracy = 100.0 * (double) numberSamplesCorrect
					/ (double) numberSamples;
			System.out.println(tag + numberSamples + " instances, " + accuracy
					+ "% accuracy");
		} else
			System.out.println(tag + numberSamples + " instances, " + "n/a"
					+ "% accuracy");
	}

	protected Instances getAccumulatedInstances() {
		return accumulatedInstances;
	}
}
