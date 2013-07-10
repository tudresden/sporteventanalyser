package de.core;

/**
 * 
 * @author Alrik Geselle
 * @version 1.0
 */
public class Goalkeeper extends Player
{
	// private int goals;
	// private int shotsOnOwnGoal;
	// private int heldShots;
	// private int goalAgainst;
	// private int passes;
	// private int successfulPasses;
	// private int ballContacts;
	// private long ballPossessionTime;
	// private int leftFootID;
	// private int rightFootID;
	// private int leftArmID;
	// private int rightArmID;
	// private Map<Integer, Integer> passesFrom;
	// private Map<Integer, Integer> passesTo;
	// private float runDistance;
	// private boolean onBall;

	public Goalkeeper(int id, Team team, String name, int leftFootID, int rightFootID, int leftArmID, int rightArmID)
	{
		super(id, team, name, PlayingPosition.GK, leftFootID, rightFootID);

		// this.leftArmID = leftArmID;
		// this.rightArmID = rightArmID;
		// this.goals = 0;
		// this.shotsOnOwnGoal = 0;
		// this.heldShots = 0;
		// this.goalAgainst = 0;
		// this.passes = 0;
		// this.successfulPasses = 0;
		// this.ballContacts = 0;
		// this.ballPossessionTime = 0;
		// this.passesFrom = new HashMap<Integer, Integer>();
		// this.passesTo = new HashMap<Integer, Integer>();
		// this.runDistance = 0;
	}

	public String toString()
	{
		return "Goalkeeper " + this.getId() + " " + this.getTimeStamp() + " (" + this.positionX + ", " + this.positionY + ", " + this.positionZ + ") (" + this.velocityX + ", " + this.velocityY + ", " + this.velocityZ + ") (" + this.accelerationX + ", " + this.accelerationY + ", " + this.accelerationZ + ")";
	}
}
