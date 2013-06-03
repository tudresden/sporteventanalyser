package de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;

public class Event extends Raw {

	public static final byte PAYLOAD_TYPE = 0;

	/**
	 * Sender (4 Byte)
	 */
	private int Sender = Integer.MIN_VALUE;

	/**
	 * Timestamp (8 Byte)
	 */
	private long Timestamp = Long.MIN_VALUE;

	/**
	 * PositionX (4 Byte)
	 */
	private int PositionX = Integer.MIN_VALUE;

	/**
	 * PositionY (4 Byte)
	 */
	private int PositionY = Integer.MIN_VALUE;

	/**
	 * PositionZ (4 Byte)
	 */
	private int PositionZ = Integer.MIN_VALUE;

	/**
	 * Velocity (4 Byte)
	 */
	private int Velocity = Integer.MIN_VALUE;

	/**
	 * Acceleration (4 Byte)
	 */
	private int Acceleration = Integer.MIN_VALUE;

	/**
	 * VelocityX (4 Byte)
	 */
	private int VelocityX = Integer.MIN_VALUE;

	/**
	 * VelocityY (4 Byte)
	 */
	private int VelocityY = Integer.MIN_VALUE;

	/**
	 * VelocityZ (4 Byte)
	 */
	private int VelocityZ = Integer.MIN_VALUE;

	/**
	 * AccelerationX (4 Byte)
	 */
	private int AccelerationX = Integer.MIN_VALUE;

	/**
	 * AccelerationY (4 Byte)
	 */
	private int AccelerationY = Integer.MIN_VALUE;

	/**
	 * AccelerationZ (4 Byte)
	 */
	private int AccelerationZ = Integer.MIN_VALUE;

	public Event(int Sender, long Timestamp, int PositionX, int PositionY,
			int PositionZ, int Velocity, int Acceleration, int VelocityX,
			int VelocityY, int VelocityZ, int AccelerationX, int AccelerationY,
			int AccelerationZ) {
		super(Event.PAYLOAD_TYPE);
		this.Sender = Sender;
		this.Timestamp = Timestamp;
		this.PositionX = PositionX;
		this.PositionY = PositionY;
		this.PositionZ = PositionZ;
		this.Velocity = Velocity;
		this.Acceleration = Acceleration;
		this.VelocityX = VelocityX;
		this.VelocityY = VelocityY;
		this.VelocityZ = VelocityZ;
		this.AccelerationX = AccelerationX;
		this.AccelerationY = AccelerationY;
		this.AccelerationZ = AccelerationZ;
	}

	public Event() {
		super(Event.PAYLOAD_TYPE);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(Sender);
		out.writeLong(Timestamp);
		out.writeInt(PositionX);
		out.writeInt(PositionY);
		out.writeInt(PositionZ);
		out.writeInt(Velocity);
		out.writeInt(Acceleration);
		out.writeInt(VelocityX);
		out.writeInt(VelocityY);
		out.writeInt(VelocityZ);
		out.writeInt(AccelerationX);
		out.writeInt(AccelerationY);
		out.writeInt(AccelerationZ);
		out.flush();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.Sender = in.readInt();
		this.Timestamp = in.readLong();
		this.PositionX = in.readInt();
		this.PositionY = in.readInt();
		this.PositionZ = in.readInt();
		this.Velocity = in.readInt();
		this.Acceleration = in.readInt();
		this.VelocityX = in.readInt();
		this.VelocityY = in.readInt();
		this.VelocityZ = in.readInt();
		this.AccelerationX = in.readInt();
		this.AccelerationY = in.readInt();
		this.AccelerationZ = in.readInt();
	}

	/**
	 * @return the sender
	 */
	public int getSender() {
		return Sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(int sender) {
		Sender = sender;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return Timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		Timestamp = timestamp;
	}

	/**
	 * @return the positionX
	 */
	public int getPositionX() {
		return PositionX;
	}

	/**
	 * @param positionX
	 *            the positionX to set
	 */
	public void setPositionX(int positionX) {
		PositionX = positionX;
	}

	/**
	 * @return the positionY
	 */
	public int getPositionY() {
		return PositionY;
	}

	/**
	 * @param positionY
	 *            the positionY to set
	 */
	public void setPositionY(int positionY) {
		PositionY = positionY;
	}

	/**
	 * @return the positionZ
	 */
	public int getPositionZ() {
		return PositionZ;
	}

	/**
	 * @param positionZ
	 *            the positionZ to set
	 */
	public void setPositionZ(int positionZ) {
		PositionZ = positionZ;
	}

	/**
	 * @return the velocity
	 */
	public int getVelocity() {
		return Velocity;
	}

	/**
	 * @param velocity
	 *            the velocity to set
	 */
	public void setVelocity(int velocity) {
		Velocity = velocity;
	}

	/**
	 * @return the acceleration
	 */
	public int getAcceleration() {
		return Acceleration;
	}

	/**
	 * @param acceleration
	 *            the acceleration to set
	 */
	public void setAcceleration(int acceleration) {
		Acceleration = acceleration;
	}

	/**
	 * @return the velocityX
	 */
	public int getVelocityX() {
		return VelocityX;
	}

	/**
	 * @param velocityX
	 *            the velocityX to set
	 */
	public void setVelocityX(int velocityX) {
		VelocityX = velocityX;
	}

	/**
	 * @return the velocityY
	 */
	public int getVelocityY() {
		return VelocityY;
	}

	/**
	 * @param velocityY
	 *            the velocityY to set
	 */
	public void setVelocityY(int velocityY) {
		VelocityY = velocityY;
	}

	/**
	 * @return the velocityZ
	 */
	public int getVelocityZ() {
		return VelocityZ;
	}

	/**
	 * @param velocityZ
	 *            the velocityZ to set
	 */
	public void setVelocityZ(int velocityZ) {
		VelocityZ = velocityZ;
	}

	/**
	 * @return the accelerationX
	 */
	public int getAccelerationX() {
		return AccelerationX;
	}

	/**
	 * @param accelerationX
	 *            the accelerationX to set
	 */
	public void setAccelerationX(int accelerationX) {
		AccelerationX = accelerationX;
	}

	/**
	 * @return the accelerationY
	 */
	public int getAccelerationY() {
		return AccelerationY;
	}

	/**
	 * @param accelerationY
	 *            the accelerationY to set
	 */
	public void setAccelerationY(int accelerationY) {
		AccelerationY = accelerationY;
	}

	/**
	 * @return the accelerationZ
	 */
	public int getAccelerationZ() {
		return AccelerationZ;
	}

	/**
	 * @param accelerationZ
	 *            the accelerationZ to set
	 */
	public void setAccelerationZ(int accelerationZ) {
		AccelerationZ = accelerationZ;
	}

	/* (non-Javadoc)
	 * @see de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw#clone()
	 */
	public Event clone() {
		return new Event(Sender, Timestamp, PositionX, PositionY, PositionZ,
				Velocity, Acceleration, VelocityX, VelocityY, VelocityZ,
				AccelerationX, AccelerationY, AccelerationZ);
	}
}
