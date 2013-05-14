package de.esper;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.core.EventBallPossession;

public class BallPossessionListener implements UpdateListener
{
	public void update(EventBean[] newData, EventBean[] oldData)
	{
		System.out.println("BallEvent received: " + newData[0].getUnderlying());
		EventBallPossession event = ((EventBallPossession) newData[0].getUnderlying());
		System.out.println(event.name);
	}
}
