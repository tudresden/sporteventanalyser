package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class AppendingObjectInputStream extends ObjectInputStream {

	public AppendingObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	protected void readStreamHeader() {
		// NOOP
	}

}
