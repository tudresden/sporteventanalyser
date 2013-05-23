package de.esper;

import java.util.concurrent.TimeUnit;

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
		Entity newEntity = new Entity(event.getId(), event.getTimeStamp(), event.getPositionX(), event.getPositionY(), event.getPositionZ(), event.getVelocityX(), event.getVelocityY(), event.getVelocityZ(), event.getAccelerationX(), event.getAccelerationY(), event.getAccelerationZ(), event.getAcceleration(), event.getVelocity());

		if (entity instanceof Player)
		{
			Player player = (Player) entity;
			player.update(newEntity);

			// player.updateHeatmap();

			// System.out.println(player);
			// System.out.println(player.getTotalDistance());
			// System.out.println("-->" + player.getHeatmap().getSum());
			// if (player.getHeatmap().getSum()==50)
			// player.getHeatmap().drawGrid();
		}
		else if (entity instanceof Ball)
		{
			Ball ball = (Ball) entity;
			ball.update(newEntity);
			Player nearestPlayer = Utils.getNearestPlayer(Main.main.getEventDecoder(), ball);

			Utils.shotOnGoal(Main.main.getEventDecoder(), ball);
			/*if (nearestPlayer != null && Utils.getBallHit(Main.main.getEventDecoder(), nearestPlayer, ball))
			{
				int duration = Utils.convertTimeToOffset(ball.getTimeStamp());

				if (duration >= 0 && Main.main.currentBallPossessionId != nearestPlayer.id)
				{
					Main.main.currentBallPossessionId = nearestPlayer.id;

					System.out.println("--------------");
					// print game time
					String time = String.format("%d min, %d sec", TimeUnit.SECONDS.toMinutes(duration), TimeUnit.SECONDS.toSeconds(duration) % 60);
					System.out.println("Spielzeit: " + time);
					
					System.out.println("Team: " + nearestPlayer.getTeam());
					System.out.println("Name des Spielers am Ball: " + nearestPlayer.getName());
					System.out.println("Spieler: (ID: " + nearestPlayer.getId() + ") --- Zeitstempel: " + nearestPlayer.getTimeStamp());
					System.out.println("Ball:    (ID: 0" + ball.id + ") --- Zeitstempel: " + ball.getTimeStamp());
					
				}
			}
			else
			{
				// no one has the ball
				Main.main.currentBallPossessionId = 0;
			}*/
		}
		else if (entity instanceof Goalkeeper)
		{
			Goalkeeper goalkeeper = (Goalkeeper) entity;
			goalkeeper.update(newEntity);
			System.out.println(goalkeeper);
		}
	}
}
