package de.tudresden.inf.rn.mapper.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * This is used as an auxiliary mapper to store data of the "full-game"-file in
 * another way
 */
public class GameFileMapper {

	public GameFileMapper() {
	}

	/**
	 * Restores the data of the inURI as byte-output
	 * 
	 * @param inURI
	 * @param outURI
	 */
	public void restore(String inURI, String outURI) {
		try {
			// RandomAccessFile inRAF = new RandomAccessFile(new File(inURI),
			// "r");

			// RandomAccessFile outRAF = new RandomAccessFile(outFile, "rw");
			// inRAF.seek(0);
			// outRAF.seek(0);

			// Bench-stats
			long n = System.currentTimeMillis();
			int i = 0;

			File oF = new File(outURI);
			if (!oF.exists())
				oF.createNewFile();
			FileChannel iCh = new FileInputStream(inURI).getChannel();
			FileChannel oCh = new FileOutputStream(oF).getChannel();
			ByteBuffer iBb = ByteBuffer.allocateDirect(262144);
			ByteBuffer oBb = ByteBuffer.allocateDirect(262136);

			char[] cA = new char[19];
			short c = 0;
			int eC = 0;
			int nR;
			byte bR;
			System.out.println("Converting data...");
			while ((nR = iCh.read(iBb)) != -1) {
				iBb.position(0);
				iBb.limit(nR);
				while (iBb.hasRemaining()) {
					if ((bR = iBb.get()) == 13) {
						// NOOP
					} else if (bR == 10) {
						// And append new Integer
						oBb.putInt(Integer.valueOf(String.valueOf(cA, 0, c)));
						c = 0;
						eC = 0;

						// Bench
						i++;
						if (i % 1000000 == 0) {
							System.out.println("Entries written: " + i);
						}

						// Check whether oBb is full
						if (!oBb.hasRemaining()) {
							oBb.flip();
							oCh.write(oBb);
							oBb.clear();
						}
					} else if (bR == 44) {
						// Write value to oBb
						if (eC == 1)
							oBb.putLong(Long.valueOf(String.valueOf(cA, 0, c)));
						else
							oBb.putInt(Integer.valueOf(String.valueOf(cA, 0, c)));
						c = 0;
						eC++;
					} else {
						cA[c] = (char) bR;
						c++;
					}
				}
				iBb.clear();
			}
			oBb.flip();
			oCh.write(oBb);
			oBb.clear();
			iCh.close();
			oCh.close();

			// String e = null;
			// String eD[] = null;
			// System.out.println("Converting data...");
			// while ((e = inRAF.readLine()) != null) {
			// eD = e.split(",");
			// outRAF.writeInt(Integer.valueOf(eD[0]));
			// outRAF.writeDouble(Long.valueOf(eD[1]));
			// outRAF.writeInt(Integer.valueOf(eD[2]));
			// outRAF.writeInt(Integer.valueOf(eD[3]));
			// outRAF.writeInt(Integer.valueOf(eD[4]));
			// outRAF.writeInt(Integer.valueOf(eD[5]));
			// outRAF.writeInt(Integer.valueOf(eD[6]));
			// outRAF.writeInt(Integer.valueOf(eD[7]));
			// outRAF.writeInt(Integer.valueOf(eD[8]));
			// outRAF.writeInt(Integer.valueOf(eD[9]));
			// outRAF.writeInt(Integer.valueOf(eD[10]));
			// outRAF.writeInt(Integer.valueOf(eD[11]));
			// outRAF.writeInt(Integer.valueOf(eD[12]));
			// i++;
			// if (i % 1000000 == 0) {
			// System.out.println("Entries written: " + i);
			// }
			// }
			// outRAF.close();

			System.out
					.println("Data converted successfully (Total amount of entries: "
							+ i
							+ ", elapsed time: "
							+ (System.currentTimeMillis() - n) + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GameFileMapper().restore(
				"C:\\Users\\Concept-X\\Desktop\\full-game",
				"H:\\transformed-game");
	}

}
