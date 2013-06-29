package predictions;

import weka.core.Instance;
import moa.core.InstancesHeader;

/**
 * Encapsulates an instance for training and prediction.
 * 
 */
public abstract class PredictionInstance {

	/**
	 * Instantiates the prediction instance and initializes it.
	 */
	public PredictionInstance() {
		init();
	}

	/**
	 * Initializes the instance: creates attributes and header.
	 */
	abstract void init();

	/**
	 * Returns the instance header.
	 * 
	 * @return the instance header
	 */
	abstract InstancesHeader getHeader();

	/**
	 * Returns the instance.
	 * 
	 * @return the current instance
	 */
	abstract Instance getInstance();

	/**
	 * Returns the instance.
	 * 
	 * @return copy of current instance
	 */
	abstract Instance getInstanceCopy();

}
