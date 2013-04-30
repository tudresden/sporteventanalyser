package de.tudresden.inf.rn.mobilis.generator.interfaces;

public class Event implements Cloneable {

	private int Sender = Integer.MIN_VALUE;
	private long Timestamp = Long.MIN_VALUE;
	private int PositionX = Integer.MIN_VALUE;
	private int PositionY = Integer.MIN_VALUE;
	private int PositionZ = Integer.MIN_VALUE;
	private int Velocity = Integer.MIN_VALUE;
	private int Acceleration = Integer.MIN_VALUE;
	private int VelocityX = Integer.MIN_VALUE;
	private int VelocityY = Integer.MIN_VALUE;
	private int VelocityZ = Integer.MIN_VALUE;
	private int AccelerationX = Integer.MIN_VALUE;
	private int AccelerationY = Integer.MIN_VALUE;
	private int AccelerationZ = Integer.MIN_VALUE;

	public Event(int Sender, long Timestamp, int PositionX, int PositionY,
			int PositionZ, int Velocity, int Acceleration, int VelocityX,
			int VelocityY, int VelocityZ, int AccelerationX, int AccelerationY,
			int AccelerationZ) {
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

	public int getSender() {
		return Sender;
	}

	public long getTimestamp() {
		return Timestamp;
	}

	public int getPositionX() {
		return PositionX;
	}

	public int getPositionY() {
		return PositionY;
	}

	public int getPositionZ() {
		return PositionZ;
	}

	public int getVelocity() {
		return Velocity;
	}

	public int getAcceleration() {
		return Acceleration;
	}

	public int getVelocityX() {
		return VelocityX;
	}

	public int getVelocityY() {
		return VelocityY;
	}

	public int getVelocityZ() {
		return VelocityZ;
	}

	public int getAccelerationX() {
		return AccelerationX;
	}

	public int getAccelerationY() {
		return AccelerationY;
	}

	public int getAccelerationZ() {
		return AccelerationZ;
	}

	public Event clone() {
		return new Event(Sender, Timestamp, PositionX, PositionY, PositionZ,
				Velocity, Acceleration, VelocityX, VelocityY, VelocityZ,
				AccelerationX, AccelerationY, AccelerationZ);
	}
}
