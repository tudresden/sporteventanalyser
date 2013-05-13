package de.esper;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.core.Main;

public class CEPListener implements UpdateListener
{
	public void update(EventBean[] newData, EventBean[] oldData)
	{
		System.out.println("Event received: " + newData[0].getUnderlying());

		Main.main.getEventDecoder().getPlayerList();
	}
}