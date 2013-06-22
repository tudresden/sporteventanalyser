package predictions;

import moa.core.InstancesHeader;

public abstract class Learner {

	public Learner(InstancesHeader instanceHeader) {
		init(instanceHeader);
	}

	protected abstract void init(InstancesHeader instanceHeader);

	abstract void train(PredictionInstance trainingInstance);

	abstract int getPrediction(PredictionInstance predictionInstance);

}
