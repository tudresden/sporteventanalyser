package predictions;

import weka.core.Instance;
import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingTree;

public class GoalPredictor {

	int numberSamplesCorrect;
	int numberSamples;
	Classifier learner;
	GoalPredictionInstanceManager instanceManager;

	public GoalPredictor() {
		init();
	}

	private void init() {
		learner = new HoeffdingTree();
		instanceManager = new GoalPredictionInstanceManager();

		learner.setModelContext(instanceManager.getHeader());
		learner.prepareForUse();

		numberSamplesCorrect = 0;
		numberSamples = 0;
	}

	public void onEvent(String event) {
		Instance trainingInstance = instanceManager.getTrainingInstance(event);
		learner.trainOnInstance(trainingInstance);

		numberSamples++;
	}

	public void onPlayerChange(String nameOfPlayerWithBall) {
		// count ball possession of player
		instanceManager.updateInstance(nameOfPlayerWithBall);

	}
}
