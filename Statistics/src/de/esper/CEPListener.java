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
			}
			else if (entity instanceof Ball)
			{
				Ball ball = (Ball) entity;

				/*
				 * Return if ball is no within the game field.
				 */
				if (Main.main.currentActiveBallId != 0 && !Utils.positionWithinField(event.getPositionX(), event.getPositionY()))
				{
					// if (Main.main.currentActiveBallId == ball.getId())
					// {
					// System.out.println("############------------------------!!!---au�erhalb des Spielfeldes---!!!--------------------------------############");
					// }
					// Main.main.currentActiveBallId = 0;
					return;
				}
				else
				{
					if (Main.main.currentActiveBallId != ball.getId())
					{
						Main.main.currentActiveBallId = ball.getId();
						System.out.println("============================================");
						System.out.println("BALLID " + Main.main.currentActiveBallId);
						System.out.println("============================================");
					}
				}

				ball.update(event);

				Player nearestPlayer = Utils.getNearestPlayer(Main.main.getEventDecoder(), ball);

				// Ball Possesion for the same player
				long zeit = nearestPlayer.timeStamp - Main.main.timePlayer;
				Main.main.timeAllPlayer += zeit;
				if (Main.main.timeAllPlayer >= Math.pow(10, 12))
				{
					Main.main.currentBallPossessionId = 0;
					Main.main.timeAllPlayer = 0;
				}

				if (!Utils.positionWithinField(event.getPositionX(), event.getPositionY()))
				{
					if (Main.main.out == 0)
					{
						System.out.println("############------------------------!!!---au�erhalb des Spielfeldes---!!!--------------------------------############");
						Main.main.out = 1;
					}
				}

				else
					Main.main.out = 0;

				// Utils.shotOnGoal(Main.main.getEventDecoder(), ball);
			
				if (nearestPlayer != null)
				{			
					//Function for BallContacts
					if (Main.main.currentBallPossessionId != nearestPlayer.id && Utils.getBallHit(Main.main.getEventDecoder(), nearestPlayer, ball))
					{
						Main.main.currentBallPossessionId = nearestPlayer.id;	
//						System.out.println("--------------");
						// print game time
						String time = String.format("%d min, %d sec", TimeUnit.SECONDS.toMinutes(gameTime), TimeUnit.SECONDS.toSeconds(gameTime) % 60);
//						System.out.println("Spielzeit: " + time);
//
//						System.out.println("Team: " + nearestPlayer.getTeam());
//						System.out.println("Name des Spielers am Ball: " + nearestPlayer.getName());
//						System.out.println("Laufstrecke: " + nearestPlayer.getTotalDistance() / 1000);

						nearestPlayer.setBallContacts(nearestPlayer.getBallContacts() + 1);
//						System.out.println("Ballkontakte: " + nearestPlayer.getBallContacts());
						
						//Function for Passes
						if (Main.main.currentPlayer != null){
							if (Utils.pass(Main.main.currentPlayer, nearestPlayer)==1)
							{
								Player Heinz = Main.main.currentPlayer;
								Heinz.setSuccessfulPasses(Heinz.getSuccessfulPasses()+1);
								System.out.println("Spielzeit: " + time);
								System.out.println(Heinz.getName()+" - Erfolgreiche P�sse: "+Heinz.getSuccessfulPasses());
							}
							if (Utils.pass(Main.main.currentPlayer, nearestPlayer)==2)
							{
								Player Heinz = Main.main.currentPlayer;
								Heinz.setMissedPasses(Heinz.getMissedPasses()+1);
								System.out.println("Spielzeit: " + time);
								System.out.println(Heinz.getName()+" - Fehlgeschlagene P�sse: "+Heinz.getMissedPasses());
							}
						}
						Main.main.currentPlayer = nearestPlayer;
						// if (Main.main.shit)
						// {
						// System.out.println("kick! " + ball.getAcceleration());
						// }

						// System.out.println("Spieler: (ID: " + nearestPlayer.leftFootID + ", " + nearestPlayer.rightFootID +
						// ") --- Zeitstempel: " + nearestPlayer.getTimeStamp());
						// System.out.println("Ball:    (ID: 0" + ball.id + ") --- Zeitstempel: " + ball.getTimeStamp());
						//Successful Passes
					}

				}
				else
				{
					// no one has the ball

					// Main.main.currentBallPossessionId = 0;
				}
				Main.main.timePlayer = nearestPlayer.timeStamp;
				
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
