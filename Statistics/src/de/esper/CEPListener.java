package de.esper;

import java.util.concurrent.TimeUnit;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.core.Ball;
import de.core.Entity;
import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.impl.Event;
import de.core.Goalkeeper;
import de.core.Main;
import de.core.Player;
import de.core.Utils;

public class CEPListener implements UpdateListener
{
	public void update(EventBean[] newData, EventBean[] oldData)
	{
		Event event = ((Event) newData[0].getUnderlying());
		Entity entity = Main.main.getEntityFromId(event.getSender());
		final int gameTime = Utils.convertTimeToOffset(event.getTimestamp());

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
				if (!Utils.positionWithinField(event.getPositionX(), event.getPositionY()))
				{
					if (Main.main.currentActiveBallId != 0 && Main.main.currentActiveBallId == ball.getId())
					{
						System.out.println("============================================");
						System.out.println("Ball ID " + ball.getId() + " im außerhalb des Spielfeldes!");
						System.out.println("============================================");
						Main.main.currentActiveBallId = 0;
					}

					return;
				}
				else
				{
					if (Main.main.currentActiveBallId != ball.getId())
					{
						Main.main.currentActiveBallId = ball.getId();
						System.out.println("============================================");
						System.out.println("Aktuelle Ball ID im Spiel: " + Main.main.currentActiveBallId);
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

				// Utils.shotOnGoal(Main.main.getEventDecoder(), ball);

				if (nearestPlayer != null)
				{
					// Function for BallContacts
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

						nearestPlayer.setBallContacts(nearestPlayer.getBallContacts() + 1);
						System.out.println("Ballkontakte: " + nearestPlayer.getBallContacts());

						// Function for Passes
						Player lastPlayer = Main.main.currentPlayer;

						if (lastPlayer != null)
						{
							if (Utils.pass(Main.main.currentPlayer, nearestPlayer) == 1)
							{
								lastPlayer.setSuccessfulPasses(lastPlayer.getSuccessfulPasses() + 1);
								System.out.println("Spielzeit: " + time);
								System.out.println(lastPlayer.getName() + " - Erfolgreiche Pässe: " + lastPlayer.getSuccessfulPasses());
							}
							if (Utils.pass(Main.main.currentPlayer, nearestPlayer) == 2)
							{
								lastPlayer.setMissedPasses(lastPlayer.getMissedPasses() + 1);
								System.out.println("Spielzeit: " + time);
								System.out.println(lastPlayer.getName() + " - Fehlgeschlagene Pässe: " + lastPlayer.getMissedPasses());
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
						// Successful Passes
					}

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