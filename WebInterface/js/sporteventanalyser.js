var sea = {

	HTTPBIND : "http://127.0.0.1:7070/http-bind/",

	/** Function: getGameMappings
	 *  Get mappings of the game (size of the gamefield, mappings for each player, position of goals)
	 *
	 *  Parameters:
	 *    (Function) onResult - Callback function when result received
	 *    (Function) onTimeout - Callback function when no result has been received (result timed out)
	 */
	getGameMappings : function(onResult, onTimeout) {
		Mobilis.sporteventanalyser.GameMappings(new Mobilis.sporteventanalyser.ELEMENTS.MappingRequest(), onResult, onTimeout);
	},

	/** Function: connect
	 *  Establishes a connection to the MobilisServer
	 *
	 *  Parameters:
	 *    (String) uFullJid - Full JID of the user (e.g. seaclient@sea/jsclient)
	 *    (String) uPassword - Password for this jid
	 *    (String) mBareJid - Bare JID of the server (e.g. mobilis@sea)
	 *    (Function) onSuccess - Callback function when successfully connected
	 */
	connect : function(uFullJid, uPassword, mBareJid, onSuccess) {
		Mobilis.utils.trace("Trying to establish a connection to Mobilis");
		Mobilis.core.connect(uFullJid, uPassword, mBareJid, sea.HTTPBIND, function() {
			// sea.addHandlers();
			onSuccess && onSuccess();
		});
	}
}
var sea_default_setup = function() {
	sea.connect("seaclient@sea", "sea", "mobilis@sea", function() {
		// Do something special here!

		sea.getGameMappings(function(Mappings) {
			var gameField = Mappings.GameFieldSize;
			var goals = Mappings.Goals;
			var playerMappings = Mappings.PlayerMappings;
			console.log("Gamefield parameters:");
			console.log("Min-X: " + gameField.GameFieldMinX + " Max-X: " + gameField.GameFieldMaxX);
			console.log("Min-Y: " + gameField.GameFieldMinY + " Max-Y: " + gameField.GameFieldMaxY);
			console.log("-------------------------------------------------");
			console.log("Goal 1 parameters:");
			console.log("Min-X: " + goals[0].GoalMinX + " Max-X: " + goals[0].GoalMaxX);
			console.log("Goal 2 parameters:");
			console.log("Min-X: " + goals[1].GoalMinX + " Max-X: " + goals[1].GoalMaxX);
			console.log("-------------------------------------------------");
			$.each(playerMappings, function(i, v) {
				console.log("Player: " + v.PlayerName + " (ID: " + v.PlayerID + ", Team: " + v.TeamName + ")");
			});
		}, function() {
			// TODO: Remove this sample function
			console.log("Timed out!");
		});

		sea.pubsub.subscribeStatistic();

		sea.pubsub.addCurrentPositionDataHandler(function(item) {
			// console.log(item);
		});
		sea.pubsub.addCurrentPlayerDataHandler(function(item) {
			// console.log(item);
		});
		sea.pubsub.addCurrentTeamDataHandler(function(item) {
			// console.log(item);
		});
		sea.pubsub.addCurrentHeatMapDataHandler(function(item) {
			// console.log(item);
		});
		sea.pubsub.addCurrentPrognosisDataHandler(function(item) {
			// console.log(item);
		});
		sea.pubsub.addCurrentGameDataHandler(function(item) {
			// console.log(item);
		});
	});
	return sea;
}
