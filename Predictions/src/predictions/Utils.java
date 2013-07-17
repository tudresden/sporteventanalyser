package predictions;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * Utility class.
 * 
 */
public class Utils {

	/**
	 * If set to <i>true</i>, console outputs for debugging will be generated.
	 */
	public static final boolean WRITE_DEBUGGING_LOGS = false;

	/**
	 * If set to <i>true</i>, info console outputs will be generated.
	 */
	public static final boolean WRITE_INFO_LOGS = true;

	/**
	 * If set to <i>true</i>, all instances will be collected and an ARFF file
	 * will be created at the end.
	 */
	public static final boolean ARFF_WRITING_MODE = false;

	/**
	 * Own side of the field.
	 */
	public static final String FIELD_AREA_OWN_TEAM = "FIELD_AREA_OWN_TEAM";

	/**
	 * Middle of the playing field.
	 */
	public static final String FIELD_AREA_MIDDLE = "FIELD_AREA_MIDDLE";

	/**
	 * Opponent's playing field side.
	 */
	public static final String FIELD_AREA_OPPONENTS = "FIELD_AREA_OPPONENTS";

	/**
	 * Determines the current field area of a player.
	 * 
	 * @param y
	 *            the vertical position on the field
	 * @param onOpponentSide
	 *            if current player is on the opponents side
	 * @return the area, can be: <i>FIELD_AREA_OWN_TEAM</i>,
	 *         <i>FIELD_AREA_MIDDLE</i>, <i>FIELD_AREA_OPPONENTS</i>
	 */
	public static final String getFieldArea(int y, boolean onOpponentSide) {

		// top field side
		if (y > 10000) {
			return onOpponentSide ? FIELD_AREA_OPPONENTS : FIELD_AREA_OWN_TEAM;
		}
		// bottom field side
		else if (y < -10000) {
			return onOpponentSide ? FIELD_AREA_OPPONENTS : FIELD_AREA_OWN_TEAM;
		}
		// middle of field
		else {
			return FIELD_AREA_MIDDLE;
		}

	}

	/**
	 * Creates an ARFF file from a set of instances.
	 * 
	 * @param dataSet
	 *            all collected instances
	 * @param fileName
	 *            the file name
	 */
	public static void createArffFileFromInstances(Instances dataSet,
			String fileName) {

		if (WRITE_INFO_LOGS)
			System.out.println("Creating arff file from " + dataSet.size()
					+ " prediction instances.");

		// create unique file name
		int fileNumber = 0;
		String filePathString = "";
		while (new File(filePathString = "../Predictions/prediction_logs/"
				+ fileName + "_" + (++fileNumber) + ".arff").exists())
			;

		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		try {
			saver.setFile(new File(filePathString));
			saver.writeBatch();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}
