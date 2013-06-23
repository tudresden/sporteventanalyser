package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media;

import java.io.Externalizable;

/**
 * A <code>Raw</code> is used as communication <code>Object</code> between the
 * transmitter and a receiver. Therefore a concrete <code>Raw</code> has to
 * implement the <code>Object.clone()</code> method as well as the
 * <code>Externalizable</code> <code>interface</code> to (de-)serialize the
 * <code>Raw</code> <code>Object</code> into a binary representation
 */
public abstract class Raw implements Externalizable, Cloneable {

	/**
	 * Identifier to determine the type of this payload
	 */
	private final byte payloadType;

	/**
	 * Constructor which forces to set the estimated length of an encoded
	 * <code>Object</code>
	 * 
	 * @param payloadType
	 *            Identifier to determine the type of this payload
	 */
	public Raw(byte payloadType) {
		this.payloadType = payloadType;
	}

	/**
	 * Get the type of this payload
	 * 
	 * @return type of this payload
	 */
	public byte getPayloadType() {
		return payloadType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + payloadType;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Raw other = (Raw) obj;
		if (payloadType != other.getPayloadType()) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public abstract Raw clone();
}
