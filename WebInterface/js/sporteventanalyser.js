var sea = {

	HTTPBIND : "http://127.0.0.1:7070/http-bind/",

	/** Function: getPlayerMappings
	 *  Get mappings of all players (Receives an array of Mapping-Elements)
	 *
	 *  Parameters:
	 *    (Function) onResult - Callback function when result received
	 *    (Function) onTimeout - Callback function when no result has been received (result timed out)
	 */
	getPlayerMappings : function(onResult, onTimeout) {
		Mobilis.sporteventanalyser.PlayerMappings(new Mobilis.sporteventanalyser.ELEMENTS.MappingRequest(), onResult, onTimeout);
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

$(function() {
	$("#playerstatsa").click(function(event) {
		sea.connect("seaclient@sea/Client", "sea", "mobilis@sea", function() {
			// Do something special here!

			// Mobilis.connection.pubsub.discoverNodes(function(iq) {
				// console.log(iq);
			// }, function(iq) {
				// console.log(iq);
			// });
			var cT = Mobilis.utils.getUnixTime();
			var i = 0;
			sea.pubsub.subscribeCurrentPositionData(function(item) {
				i++;
				if (i % 100 == 0) {
					console.log((Mobilis.utils.getUnixTime() - cT) + " ms");
					console.log(item);
				}
				// console.log(item);
			});
			sea.pubsub.subscribeCurrentPlayerData(function(item) {
				if (i % 100 == 0) {
					console.log(item);
				}
			})
			// Mobilis.connection.pubsub.subscribe("CurrentPositionData", [], function(iq) {
				// // console.log(iq);
				// //
				// // Return true ist wegen Strophe Handlermanagement mandatory! Ansonsten wird Handler geloescht: Loesung wuerde entsprechend mit Decorator ideal umgesetzt
				// return true;
			// }, function(iq) {
				// // Result: Ist wegen XEP-0060 mandatory, aber beinhaltet keine Informationen: Nur eine Art "Okay, du bist angemeldet" Nachricht
				// console.log(iq);
// 
				// return false;
			// }, function(iq) {
				// console.log(iq);
			// });
		});
	});
});
