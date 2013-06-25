package predictions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import weka.core.Instance;

public class Utils {

	public static final String FIELD_AREA_OWN_TEAM = "FIELD_AREA_OWN_TEAM";
	public static final String FIELD_AREA_MIDDLE = "FIELD_AREA_MIDDLE";
	public static final String FIELD_AREA_OPPONENTS = "FIELD_AREA_OPPONENTS";
	public int single=0;
	
	public static final String getFieldArea(int x, boolean onOpponentSide) {
		final int totalWidth = 2 * 52500;

		// TODO determine boolean onOpponentSide

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
	
	public void logArff(Instance predictionInstance){
		try {
			PrintWriter out = new PrintWriter(new FileWriter("log.dataset.arff",true));
			String str="";
			String header="";

			int max=predictionInstance.numAttributes();
			
			for(int i=0;i<max ;i++){
				if(this.single==0)
					header+="@Attribute "+predictionInstance.attribute(i).name()+"\n";
				str+=predictionInstance.attribute(i).value(0)+",";

			}
			if(this.single==0){
				out.write(clean(header)+"\n");
				this.single=1;
			}
			
			
			out.write(clean(str)+"\n");
			out.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	public void logIntToCsv(Integer dataSet[]){
		try {
			PrintWriter out = new PrintWriter(new FileWriter("log.dataset.csv",true));
			String str="";
			String header="";
			int counter=0;
			for(int data : dataSet){
				if(this.single==0)
					header+="data"+counter+",";
				str+=data+",";
				counter++;
			}
			if(this.single==0){
				out.write(clean(header)+"\n");
				this.single=1;
			}
			
			
			out.write(clean(str)+"\n");
			out.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void logInt(Integer dataSet[]){
		try {
			PrintWriter out = new PrintWriter(new FileWriter("log.dataset.arff",true));
			String str="";
			String header="";
			int counter=0;
			for(int data : dataSet){
				if(this.single==0)
					header+="@attribute data"+counter+" numeric \n";
				str+=data+",";
				counter++;
			}
			if(this.single==0){
				out.write(clean(header)+"\n @data \n");
				this.single=1;
			}
			
			
			out.write(clean(str)+"\n");
			out.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public String clean(String str) {

		  if (str.length() > 0) {
		    str = str.substring(0, str.length()-1);
		  }
		  return str;
		}
	
	
}