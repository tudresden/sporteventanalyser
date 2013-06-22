package predictions;

import de.core.GameInformation;

public class PassSuccessPredictor implements Predictor {

	private Learner learner;
	private PredictionInstance predictionInstance;

	@Override
	public void init() {
		// TODO Auto-generated method stub

		learner = new knnLearner();
		predictionInstance = new PassSuccessPredictionInstance();
	}

	@Override
	public void update(GameInformation gameInformation) {
		// TODO Auto-generated method stub

	}

}
