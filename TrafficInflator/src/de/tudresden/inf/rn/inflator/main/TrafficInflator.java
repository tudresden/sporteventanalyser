package de.tudresden.inf.rn.inflator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.LinkedBlockingQueue;

import org.jivesoftware.smack.XMPPException;

import de.tudresden.inf.rn.inflator.reader.Reader;
import de.tudresden.inf.rn.mobilis.sea.client.connection.MobilisConnectionManager;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;

public class TrafficInflator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MobilisConnectionManager manager = new MobilisConnectionManager();
		manager.connect("127.0.0.1", 5222, "localhost");
		manager.performLogin("seaclient", "sea", "SEAClient", "mobilis@sea/SEA");
		try {
			LinkedBlockingQueue<Raw> queue = manager
					.establishJingleConnection("mobilis@sea/SEA");
			new Reader(new File("H:\\t-full-game"), new File("H:\\i-full-game"), queue).start(); // "C:\\Users\\Concept-X\\Desktop\\full-game"
		} catch (FileNotFoundException | XMPPException e) {
			e.printStackTrace();
		}
	}

}
