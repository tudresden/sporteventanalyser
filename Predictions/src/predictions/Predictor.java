package predictions;

import de.core.GameInformation;

/**
 * Predictors main class.
 * 
 */
public abstract class Predictor {

	/**
	 * The learner which is responsible for the predictions.
	 */
	protected Learner learner;
	/**
	 * The prediction instance which will used for training and prediction.
	 */
	protected PredictionInstance predictionInstance;

	/**
	 * Instantiates a predictor.
	 * 
	 * @param learner
	 *            the classifier to use for prediction
	 */
	public Predictor(Learner learner) {
		this.learner = learner;
		init();
	}

	/**
	 * Initializes the predictor.
	 */
	abstract void init();

	/**
	 * Updates the prediction. May train the learner.
	 * 
	 * @param gameInformation
	 *            reference to the game statistics
	 */
	abstract void update(GameInformation gameInformation);

	/**
	 * Make prediction without training the learner.
	 * 
	 * @param gameInformation
	 *            reference to the game statistics
	 */
	abstract void predict(GameInformation gameInformation);

	/**
	 * Train the classifier. Call only if class event occurred.
	 * 
	 * @param gameInformation
	 *            reference to the game statistics
	 * @param classAttribute
	 *            class name of occurred event
	 */
	abstract void train(GameInformation gameInformation, String classAttribute);

}
