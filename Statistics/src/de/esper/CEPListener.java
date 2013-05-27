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
		// Entity newEntity = new Entity(event.getId(), event.getTimeStamp(), event.getPositionX(), event.getPositionY(),
		// event.getPositionZ(), event.getVelocityX(), event.getVelocityY(), event.getVelocityZ(), event.getAccelerationX(),
		// event.getAccelerationY(), event.getAccelerationZ(), event.getAcceleration(), event.getVelocity());

		int gameTime = Utils.convertTimeToOffset(event.getTimeStamp());

		if (gameTime >= 0)
		{
			if (entity instanceof Player)
			{
				Player player = (Player) entity;

				player.update(event);

				// double dist = (double) player.getVelocity() / 1000000;
				//
				// if (dist > 12.0f)
				// {
				// System.out.println("--------------");
				// System.out.println("Laufstrecke: " + dist);
				// System.out.println("Name des Spielers am Ball: " + player.getName());
				// int duration = Utils.convertTimeToOffset(player.getTimeStamp());
				// String time = String.format("%d min, %d sec", TimeUnit.SECONDS.toMinutes(duration), TimeUnit.SECONDS.toSeconds(duration)
				// %
				// 60);
				// System.out.println("Spielzeit: " + time);
				// System.out.println("Team: " + player.getTeam());
				// }

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

				ball.update(event);

				Player nearestPlayer = Utils.getNearestPlayer(Main.main.getEventDecoder(), ball);

				// Utils.shotOnGoal(Main.main.getEventDecoder(), ball);

				if (nearestPlayer != null)
				{
					if (Main.main.currentBallPossessionId != nearestPlayer.id && Utils.getBallHit(Main.main.getEventDecoder(), nearestPlayer, ball))
					{
						Main.main.currentBallPossessionId = nearestPlayer.id;

						System.out.println("--------------");
						// print game time
						String time = String.format("%d min, %d sec", TimeUnit.SECONDS.toMinutes(gameTime), TimeUnit.SECONDS.toSeconds(gameTime) % 60);
						System.out.println("Spielzeit: " + time);

						System.out.println("Team: " + nearestPlayer.getTeam());
						System.out.println("Name des Spielers am Ball: " + nearestPlayer.getName());
						System.out.println("Laufstrecke: " + nearestPlayer.getTotalDistance() / 1000);

						// if (Main.main.shit)
						// {
						// System.out.println("kick! " + ball.getAcceleration());
						// }

						// System.out.println("Spieler: (ID: " + nearestPlayer.leftFootID + ", " + nearestPlayer.rightFootID +
						// ") --- Zeitstempel: " + nearestPlayer.getTimeStamp());
						// System.out.println("Ball:    (ID: 0" + ball.id + ") --- Zeitstempel: " + ball.getTimeStamp());
					}

				}
				else
				{
					// no one has the ball

					// Main.main.currentBallPossessionId = 0;
				}

			}
			else if (entity instanceof Goalkeeper)
			{
				Goalkeeper goalkeeper = (Goalkeeper) entity;
				goalkeeper.update(event);
				System.out.println(goalkeeper);
			}
		}
	}
}
