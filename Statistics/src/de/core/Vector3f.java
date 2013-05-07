package de.core;
/**
 * <code>Vector3f</code> defines a Vector of three float value tuple.
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Vector3f
{
	public float x, y, z;

	public Vector3f()
	{
		x = y = z = 0;
	}

	public Vector3f(float x, float y, float z)
	{
		this.set(x, y, z);
	}

	public Vector3f(Vector3f vector)
	{
		this.set(vector);
	}

	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(Vector3f vector)
	{
		this.set(vector.x, vector.y, vector.z);
	}

	public Vector3f zero()
	{
		x = y = z = 0;
		return this;
	}

	public float distanceBetweenSquared(Vector3f vector)
	{
		double dX = x - vector.x;
		double dY = y - vector.y;
		double dZ = z - vector.z;
		return (float) (dX * dX + dY * dY + dZ * dZ);
	}

	public float distanceBetween(Vector3f vector)
	{
		return (float) Math.sqrt(distanceBetweenSquared(vector));
	}

	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
