package de.esper;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.core.Entity;
import de.core.Event;
import de.core.Main;
import de.core.Player;

public class CEPListener implements UpdateListener
{
	public void update(EventBean[] newData, EventBean[] oldData)
	{
		// System.out.println("Event received: " + newData[0].getUnderlying());

		Entity entity = Main.main.getEntityFromId(((Event) newData[0].getUnderlying()).getId());

		if (entity instanceof Player)
		{
			System.out.println(entity);
		}
	}
}
