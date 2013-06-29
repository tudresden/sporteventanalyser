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
	 * If set to <i>true</i>, more console outputs will be generated.
	 */
	public static final boolean DEBUGGING = false;

	/**
	 * If set to <i>true</i>, all instances will be collected and an ARFF file
	 * will be created at the end.
	 */
	public static final boolean ARFF_WRITING_MODE = true;

	public static final String FIELD_AREA_OWN_TEAM = "FIELD_AREA_OWN_TEAM";
	public static final String FIELD_AREA_MIDDLE = "FIELD_AREA_MIDDLE";
	public static final String FIELD_AREA_OPPONENTS = "FIELD_AREA_OPPONENTS";

	// private static int single = 0;

	/**
	 * Determines the current field area of a player.
	 * 
	 * @param x
	 *            the vertical position on the field
	 * @param onOpponentSide
	 *            if current player is on the opponents side
	 * @return
	 */
	public static final String getFieldArea(int x, boolean onOpponentSide) {
		final int totalWidth = 2 * 52500;

		// left field side
		if (x < totalWidth / 3 - totalWidth / 2) {
			return onOpponentSide ? FIELD_AREA_OPPONENTS : FIELD_AREA_OWN_TEAM;
		}
		// right field side
		else if (x > 2 * totalWidth / 3 - totalWidth / 2) {
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
		System.out.println("Creating arff file from " + dataSet.size()
				+ " prediction instances.");

		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		try {
			saver.setFile(new File("./prediction_logs/" + fileName + ".arff"));
			saver.writeBatch();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	// public static void logArff(Instance predictionInstance) {
	// try {
	// PrintWriter out = new PrintWriter(new FileWriter(
	// "log.dataset.arff", true));
	// String str = "";
	// String header = "";
	//
	// int max = predictionInstance.numAttributes();
	//
	// for (int i = 0; i < max; i++) {
	// if (single == 0)
	// header += "@Attribute "
	// + predictionInstance.attribute(i).name() + "\n";
	// str += predictionInstance.attribute(i).value(0) + ",";
	//
	// }
	// if (single == 0) {
	// out.write(clean(header) + "\n");
	// single = 1;
	// }
	//
	// out.write(clean(str) + "\n");
	// out.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void logIntToCsv(Integer dataSet[]) {
	// try {
	// PrintWriter out = new PrintWriter(new FileWriter("log.dataset.csv",
	// true));
	// String str = "";
	// String header = "";
	// int counter = 0;
	// for (int data : dataSet) {
	// if (this.single == 0)
	// header += "data" + counter + ",";
	// str += data + ",";
	// counter++;
	// }
	// if (this.single == 0) {
	// out.write(clean(header) + "\n");
	// this.single = 1;
	// }
	//
	// out.write(clean(str) + "\n");
	// out.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void logInt(Integer dataSet[]) {
	// try {
	// PrintWriter out = new PrintWriter(new FileWriter(
	// "log.dataset.arff", true));
	// String str = "";
	// String header = "";
	// int counter = 0;
	// for (int data : dataSet) {
	// if (this.single == 0)
	// header += "@attribute data" + counter + " numeric \n";
	// str += data + ",";
	// counter++;
	// }
	// if (this.single == 0) {
	// out.write(clean(header) + "\n @data \n");
	// this.single = 1;
	// }
	//
	// out.write(clean(str) + "\n");
	// out.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public static String clean(String str) {
	//
	// if (str.length() > 0) {
	// str = str.substring(0, str.length() - 1);
	// }
	// return str;
	// }

}
