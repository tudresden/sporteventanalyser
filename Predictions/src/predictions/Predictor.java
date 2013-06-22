package predictions;

import de.core.GameInformation;

public interface Predictor {

	abstract void init();

	abstract void update(GameInformation gameInformation);
}
