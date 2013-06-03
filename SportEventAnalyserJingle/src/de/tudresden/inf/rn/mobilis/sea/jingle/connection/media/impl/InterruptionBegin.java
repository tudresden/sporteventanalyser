package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;

public class InterruptionBegin extends Raw {

	public static final byte PAYLOAD_TYPE = 1;

	/**
	 * Begin of Interruption (8 Byte)
	 */
	private long begin;

	public InterruptionBegin(long begin) {
		super(InterruptionBegin.PAYLOAD_TYPE);
		this.begin = begin;
	}

	public InterruptionBegin() {
		super(InterruptionBegin.PAYLOAD_TYPE);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(begin);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.begin = in.readLong();
	}

	/**
	 * @return the begin
	 */
	public long getBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(long begin) {
		this.begin = begin;
	}

	@Override
	public Raw clone() {
		return new InterruptionBegin(begin);
	}

}
