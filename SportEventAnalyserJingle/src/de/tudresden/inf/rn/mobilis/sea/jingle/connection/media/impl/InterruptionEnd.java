package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;

/**
 * The <code>InterruptionEnd</code> <code>Raw</code> is used to indicate that an
 * interruption ended
 */
public class InterruptionEnd extends Raw {

	/**
	 * The type of this payload (must be unique)
	 */
	public static final byte PAYLOAD_TYPE = 2;

	/**
	 * End of Interruption (8 Byte)
	 */
	private long end;

	/**
	 * Constructor for an <code>InterruptionEnd</code>
	 * 
	 * @param end
	 *            the end of this interruption
	 */
	public InterruptionEnd(long end) {
		super(InterruptionEnd.PAYLOAD_TYPE);
		this.end = end;
	}

	/**
	 * No-Args constructor
	 */
	public InterruptionEnd() {
		super(InterruptionEnd.PAYLOAD_TYPE);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(end);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.end = in.readLong();
	}

	/**
	 * @return the end
	 */
	public long getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(long end) {
		this.end = end;
	}

	@Override
	public Raw clone() {
		return new InterruptionEnd(end);
	}

}
