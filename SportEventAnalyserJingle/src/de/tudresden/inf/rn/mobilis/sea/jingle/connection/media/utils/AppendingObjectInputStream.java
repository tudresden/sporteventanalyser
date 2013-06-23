package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * This <code>AppendingObjectInputStream</code> is an auxiliary
 * <code>ObjectInputStream</code> wrapper to read an
 * <code>ObjectInputStream</code> which is appended
 */
public class AppendingObjectInputStream extends ObjectInputStream {

	/**
	 * Constructor for an <code>AppendingObjectInputStream</code>
	 * 
	 * @param in
	 *            the <code>InputStream</code> to read from
	 * @throws IOException
	 *             due to this wrapper this <code>IOException</code> will never
	 *             occur
	 */
	public AppendingObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	protected void readStreamHeader() {
		// NOOP
	}

}
