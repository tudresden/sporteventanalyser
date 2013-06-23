package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * This <code>AppendingObjectOutputStream</code> is an auxiliary
 * <code>ObjectOutputStream</code> wrapper to write an
 * <code>ObjectOutputStream</code> which may be appended
 */
public class AppendingObjectOutputStream extends ObjectOutputStream {

	/**
	 * Constructor for an <code>AppendingObjectOutputStream</code>
	 * 
	 * @param out
	 *            the <code>OutputStream</code> to write to
	 * @throws IOException
	 *             due to this wrapper this <code>IOException</code> will never
	 *             occur
	 */
	public AppendingObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	@Override
	protected void writeStreamHeader() throws IOException {
		reset();
	}

}
