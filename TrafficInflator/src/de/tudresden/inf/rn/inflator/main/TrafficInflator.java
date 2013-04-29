package de.tudresden.inf.rn.inflator.main;

import java.io.File;
import java.io.FileNotFoundException;

import de.tudresden.inf.rn.inflator.reader.Reader;
import de.tudresden.inf.rn.mobilis.sea.client.connection.MobilisConnectionManager;
import de.tudresden.inf.rn.mobilis.sea.client.proxy.SportEventAnalyserProxy;

public class TrafficInflator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MobilisConnectionManager manager = new MobilisConnectionManager();
		manager.connect("127.0.0.1", 5222, "localhost");
		manager.performLogin("seaclient", "sea", "SEAClient", "mobilis@sea/SEA");
		SportEventAnalyserProxy proxy = new SportEventAnalyserProxy(manager);
		try {
			Reader reader = new Reader(new File("H:\\t-full-game"), 5l, proxy); // "C:\\Users\\Concept-X\\Desktop\\full-game"
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
