package de.core;

public class Pass
{
	private int from, to;
	private long timestamp;
	private boolean successful;

	public Pass(int from, int to, boolean successful, long timestamp)
	{
		this.from = from;
		this.to = to;
		this.successful = successful;
		this.timestamp = timestamp;
	}

	public int getFrom()
	{
		return from;
	}

	public int getTo()
	{
		return to;
	}

	public boolean isSuccessful()
	{
		return successful;
	}

	public long getTimestamp()
	{
		return timestamp;
	}
}
