package predictions;

import weka.core.Instance;
import moa.core.InstancesHeader;

public interface PredictionInstance {
	
	abstract void init();
	
	abstract InstancesHeader getHeader();
	
	abstract Instance getInstanceForPrediction();

}
