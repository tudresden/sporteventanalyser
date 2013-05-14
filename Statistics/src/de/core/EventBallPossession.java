package de.core;

public class EventBallPossession
{
	public String name;
	public String state;
	public long time;

	public EventBallPossession(String name, long time, String state)
	{
		this.name = name;
		this.time = time;
		this.state = state;
	}

	@Override
	public String toString()
	{
		return "jo";
	}
}