package de.esper;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.core.Ball;
import de.core.Entity;
import de.core.Event;
import de.core.Goalkeeper;
import de.core.Main;
import de.core.Player;

public class CEPListener implements UpdateListener
{
	public void update(EventBean[] newData, EventBean[] oldData)
	{
		// System.out.println("Event received: " + newData[0].getUnderlying());
		Event event = ((Event) newData[0].getUnderlying());
		Entity entity = Main.main.getEntityFromId(event.getId());
		Entity newEntity = new Entity(event.getId(), event.getTimeStamp(), event.getPositionX(), event.getPositionY(), event.getPositionZ(), event.getVelocityX(), event.getVelocityY(), event.getVelocityZ(), event.getAccelerationX(), event.getAccelerationY(), event.getAccelerationZ());

		if (entity instanceof Player)
		{
			Player player = (Player) entity;
			player.update(newEntity);

			System.out.println(player);
			System.out.println(player.getTotalDistance());
		}
		else if (entity instanceof Ball)
		{
			Ball ball = (Ball) entity;
			System.out.println(ball);
		}
		else if (entity instanceof Goalkeeper)
		{
			Goalkeeper goalkeeper = (Goalkeeper) entity;
			System.out.println(goalkeeper);
		}
	}
}
