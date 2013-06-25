package predictions;

import weka.core.Instance;
import moa.core.InstancesHeader;

public abstract class PredictionInstance {

	abstract void init();

	abstract InstancesHeader getHeader();

	abstract Instance getInstance();

	abstract Instance getInstanceCopy();

}
