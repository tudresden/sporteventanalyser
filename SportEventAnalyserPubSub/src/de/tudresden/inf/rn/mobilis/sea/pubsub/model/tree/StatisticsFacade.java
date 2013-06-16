package de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree;

import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.interfaces.ICurrentPlayerData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.interfaces.ICurrentPositionData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.leaves.interfaces.ICurrentTeamData;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.BallPosition;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PlayerPosition;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.PlayerStatistic;
import de.tudresden.inf.rn.mobilis.sea.pubsub.model.tree.nodes.impl.TeamStatistic;

//TODO: This cmd^^
/**
 * The <code>StatisticsFacade</code> is used to bundle all available interfaces
 * of the <code>StatisticCollection</code> into one auxiliary proxy. For
 * multi-thread purposes it is also capable to delegate calls to a free
 * resource. So you may toggle the model to work with (the returned model will
 * not be filled with new data until you release it by toggling again)
 */
public class StatisticsFacade implements ICurrentPositionData,
		ICurrentPlayerData, ICurrentTeamData {

	/**
	 * The statistics model (this <code>StatisticCollection</code> will be
	 * filled with new data)
	 */
	private StatisticCollection statistics;

	/**
	 * Constructor for this <code>StatisticFacade</code>
	 */
	public StatisticsFacade() {
		statistics = new StatisticCollection();
	}

	/**
	 * Get the current <code>StatisticCollection</code>. The
	 * <code>StatisticsCollection</code> is a clone of the working model and
	 * therefore no changes are made there again
	 * 
	 * @return a clone of the current <code>StatisticCollection</code>
	 */
	public synchronized StatisticCollection getCurrentStatistics() {
		return (StatisticCollection) statistics.clone();
	}

	// TODO: Facade war der passende Begriff: Direktzugriff auf Elemente
	// ermoeglichen (Nur dann wird auch sichergestellt, dass das alles
	// synchronized ist!) -> Es kann zusätzlich intern ein Puffer erstellt
	// werden, sodass Zugriffe sofort moeglich sind
	// (allerdings nur dann sinnvoll, wenn Zugriff nur schreibend!)

	// TODO: Der entsprechende Buffer wuerde NICHT! das Model komplett abbilden,
	// sondern nur alle DataNodes in Maps (Entsprechend ein flaches Model, dass
	// absolut nicht ordentlich ist) -> Aenderungen koennen direkt durchgefuehrt
	// werden:
	// Ein zusaetzlicher Thread wuerde entsprechend mit einem Mutex den Zugriff
	// auf die Map holen und sich Daten kurz rausholen -> (sehr kurzer Zugriff)
	// Anschliessend kann er diese Daten im Model unterbringen. Es erfolgt dann
	// kein "clone" mehr auf dem Model: es wird nurnoch der Zugriff/Lesezugriff
	// pausiert -> Verzoegerung kann hier auftreten (~1-5ms * 33.33: 188.88ms ->
	// unterhalb Toleranz (24Hz) von 200ms: weiterhin fliessend!), aber der
	// deep-clone ist nicht mehr da (Der bei 30Hz vermutlich den GC recht
	// belasten koennte)

	/**
	 * Register teams. The chosen name will be used as identifier (ID)
	 * 
	 * @param firstTeamname
	 *            the name of the first team
	 * @param secondTeamname
	 *            the name of the second team
	 */
	public synchronized void registerTeams(String firstTeamname,
			String secondTeamname) {
		statistics.getCurrentTeamData().registerFirstTeam(firstTeamname,
				secondTeamname);
	}

	/**
	 * Register a new player. It is mandatory to call this method before setting
	 * the position of a player
	 * 
	 * @param id
	 *            the id of the player
	 */
	public synchronized void registerPlayer(int id) {
		// Register player: CurrentPositionData-leaf
		statistics.getCurrentPositionData().registerPlayerPosition(
				new PlayerPosition(id, 0, 0, 0, 0));

		// Register player: CurrentPositionData-leaf
		statistics.getCurrentPlayerData().registerPlayerStatistic(
				new PlayerStatistic(id, 0, 0, 0, 0, 0, 0, 0));
	}

	@Override
	public synchronized void setPositionOfPlayer(int id, int positionX,
			int positionY, int velocityX, int velocityY) {
		PlayerPosition playerPosition = statistics.getCurrentPositionData()
				.getPlayerPosition(id);
		if (playerPosition != null) {
			playerPosition.setPositionX(positionX);
			playerPosition.setPositionY(positionY);
			playerPosition.setVelocityX(velocityX);
			playerPosition.setVelocityY(velocityY);
		}
	}

	@Override
	public synchronized void setPositionOfBall(int positionX, int positionY,
			int positionZ, int velocityX, int velocityY) {
		BallPosition ballPosition = statistics.getCurrentPositionData()
				.getBallPosition();
		ballPosition.setPositionX(positionX);
		ballPosition.setPositionY(positionY);
		ballPosition.setPositionZ(positionZ);
		ballPosition.setVelocityX(velocityX);
		ballPosition.setVelocityY(velocityY);
	}

	@Override
	public synchronized void setPlayerStatistic(int id, int passesMade,
			int passesReceived, int tacklings, int tacklesWon, int goalsScored,
			int ballContacts, long possessionTime) {
		PlayerStatistic playerStatistic = statistics.getCurrentPlayerData()
				.getPlayerStatistic(id);
		if (playerStatistic != null) {
			playerStatistic.setPassesMade(passesMade);
			playerStatistic.setPassesReceived(passesReceived);
			playerStatistic.setTacklings(tacklings);
			playerStatistic.setTacklesWon(tacklesWon);
			playerStatistic.setGoalsScored(goalsScored);
			playerStatistic.setBallContacts(ballContacts);
			playerStatistic.setPossessionTime(possessionTime);
		}
	}

	@Override
	public synchronized void setPassesMade(int id, int passesMade) {
		PlayerStatistic playerStatistic = statistics.getCurrentPlayerData()
				.getPlayerStatistic(id);
		if (playerStatistic != null) {
			playerStatistic.setPassesMade(passesMade);
		}
	}

	@Override
	public synchronized void setPassesReceived(int id, int passesReceived) {
		PlayerStatistic playerStatistic = statistics.getCurrentPlayerData()
				.getPlayerStatistic(id);
		if (playerStatistic != null) {
			playerStatistic.setPassesReceived(passesReceived);
		}
	}

	@Override
	public synchronized void setTacklings(int id, int tacklings) {
		PlayerStatistic playerStatistic = statistics.getCurrentPlayerData()
				.getPlayerStatistic(id);
		if (playerStatistic != null) {
			playerStatistic.setTacklings(tacklings);
		}
	}

	@Override
	public synchronized void setTacklesWon(int id, int tacklesWon) {
		PlayerStatistic playerStatistic = statistics.getCurrentPlayerData()
				.getPlayerStatistic(id);
		if (playerStatistic != null) {
			playerStatistic.setTacklesWon(tacklesWon);
		}
	}

	@Override
	public synchronized void setGoalsScored(int id, int goalsScored) {
		PlayerStatistic playerStatistic = statistics.getCurrentPlayerData()
				.getPlayerStatistic(id);
		if (playerStatistic != null) {
			playerStatistic.setGoalsScored(goalsScored);
		}
	}

	@Override
	public synchronized void setBallContacs(int id, int ballContacts) {
		PlayerStatistic playerStatistic = statistics.getCurrentPlayerData()
				.getPlayerStatistic(id);
		if (playerStatistic != null) {
			playerStatistic.setBallContacts(ballContacts);
		}
	}

	@Override
	public synchronized void setPossessionTime(int id, long possessionTime) {
		PlayerStatistic playerStatistic = statistics.getCurrentPlayerData()
				.getPlayerStatistic(id);
		if (playerStatistic != null) {
			playerStatistic.setPossessionTime(possessionTime);
		}
	}

	@Override
	public void setBallPossession(String teamname, double ballPossession) {
		TeamStatistic firstTeamStatistic = statistics.getCurrentTeamData()
				.getTeamStatisticOfFirstTeam();
		if (firstTeamStatistic != null) {
			TeamStatistic secondTeamStatistic = statistics.getCurrentTeamData()
					.getTeamStatisticOfSecondTeam();

			if (firstTeamStatistic.getTeamname().equals(teamname)) {
				firstTeamStatistic.setBallPossession(ballPossession);
				secondTeamStatistic.setBallPossession(100 - ballPossession);
			} else if (secondTeamStatistic.getTeamname().equals(teamname)) {
				secondTeamStatistic.setBallPossession(ballPossession);
				firstTeamStatistic.setBallPossession(100 - ballPossession);
			}
		}
	}

	@Override
	public void setPassingAccuracy(String teamname, double passingAccuracy) {
		TeamStatistic teamStatistic = statistics.getCurrentTeamData()
				.getTeamStatistic(teamname);
		if (teamStatistic != null) {
			teamStatistic.setPassingAccuracy(passingAccuracy);
		}
	}
}
