package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media;

import java.io.Externalizable;

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

	// /**
	// * Encodes this <code>Object</code> into the returned <code>byte[]</code>.
	// * The returned <code>array</code> will have length
	// * <code>this.getPayloadLength()</code>
	// *
	// * @return resulting payload
	// */
	// public byte[] encode() {
	// byte[] _return = new byte[payloadLength];
	// this.encode(_return);
	// return _return;
	// }
	//
	// /**
	// * Encode an <code>Object</code> into the given <code>byte[]</code>
	// *
	// * @param buffer
	// * buffer for holding the payload
	// */
	// protected abstract void encode(byte[] buffer);
	//
	// /**
	// * Decodes the given payload into the resulting object
	// *
	// * @param payload
	// * the payload which holds the <code>Object</code>. The
	// * <code>byte[]</code> must be of length
	// * <code>this.getPayloadLength()</code>
	// * @return the decoded object
	// */
	// public abstract void decode(byte[] payload);

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

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public abstract Raw clone();
}
