package predictions;

public interface Learner {

	abstract void init();

	abstract void learn(PredictionInstance trainingInstance);

	abstract int getPrediction(PredictionInstance predictionInstance);

}
