package predictions;

import de.core.GameInformation;

public class PassSuccessPredictor implements Predictor {

	private Learner learner;
	private PredictionInstance predictionInstance;

	@Override
	public void init() {
		// TODO Auto-generated method stub

		predictionInstance = new PassSuccessPredictionInstance();
		learner = new knnLearner(predictionInstance.getHeader());

	}

	@Override
	public void update(GameInformation gameInformation) {
		// TODO Auto-generated method stub

	}

}
