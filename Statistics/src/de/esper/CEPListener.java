package de.esper;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.core.Ball;
import de.core.Entity;
import de.core.Event;
import de.core.Goalkeeper;
import de.core.Main;
import de.core.Player;
import de.core.Utils;

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
			
//			player.updateHeatmap();

//			System.out.println(player);
//			System.out.println(player.getTotalDistance());
//			System.out.println("-->" + player.getHeatmap().getSum());
//			if (player.getHeatmap().getSum()==50)
//				player.getHeatmap().drawGrid();
		}
		else if (entity instanceof Ball)
		{
			Ball ball = (Ball) entity;
			ball.update(newEntity);
			Utils.nearestPlayers(Main.main.getEventDecoder(), ball);
//			System.out.println(ball);
		}
		else if (entity instanceof Goalkeeper)
		{
			Goalkeeper goalkeeper = (Goalkeeper) entity;
			goalkeeper.update(newEntity);
			System.out.println(goalkeeper);
		}
	}
}
