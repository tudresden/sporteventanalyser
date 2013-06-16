(function() {

	sea.pubsub = {
		ELEMENTS : {
			BallPosition : function BallPosition(positionX, positionY, positionZ, velocityX, velocityY) {
				if (arguments[0] instanceof jQuery) {
					var _BallPosition = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "positionX": _BallPosition.positionX = $(this).text(); break;
							case "positionY": _BallPosition.positionY = $(this).text(); break;
							case "positionZ": _BallPosition.positionZ = $(this).text(); break;
							case "velocityX": _BallPosition.velocityX = $(this).text(); break;
							case "velocityY": _BallPosition.velocityY = $(this).text(); break;
						}
					});
				} else {
					this.positionX = positionX;
					this.positionY = positionY;
					this.positionZ = positionZ;
					this.velocityX = velocityX;
					this.velocityY = velocityY;
				}
			},
			PlayerPosition : function PlayerPosition(id, positionX, positionY, velocityX, velocityY) {
				if (arguments[0] instanceof jQuery) {
					var _PlayerPosition = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "id": _PlayerPosition.id = $(this).text();;
							case "positionX": _PlayerPosition.positionX = $(this).text(); break;
							case "positionY": _PlayerPosition.positionY = $(this).text(); break;
							case "velocityX": _PlayerPosition.velocityX = $(this).text(); break;
							case "velocityY": _PlayerPosition.velocityY = $(this).text(); break;
						}
					});
				} else {
					this.id = id;
					this.positionX = positionX;
					this.positionY = positionY;
					this.velocityX = velocityX;
					this.velocityY = velocityY;
				}
			},
			PlayerStatistic : function PlayerStatistic(id, passesMade, passesMissed, passesReceived, tacklings, tacklesWon, goalsScored, ballContacts, possessionTime) {
				if (arguments[0] instanceof jQuery) {
					var _PlayerPosition = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "id": _PlayerPosition.id = $(this).text();;
							case "passesMade": _PlayerPosition.passesMade = $(this).text(); break;
							case "passesMissed": _PlayerPosition.passesMissed = $(this).text(); break;
							case "passesReceived": _PlayerPosition.passesReceived = $(this).text(); break;
							case "tacklings": _PlayerPosition.tacklings = $(this).text(); break;
							case "tacklesWon": _PlayerPosition.tacklesWon = $(this).text(); break;
							case "goalsScored": _PlayerPosition.goalsScored = $(this).text(); break;
							case "ballContacts": _PlayerPosition.ballContacts = $(this).text(); break;
							case "possessionTime": _PlayerPosition.possessionTime = $(this).text(); break;
						}
					});
				} else {
					this.id = id;
					this.passesMade = passesMade;
					this.passesMissed = passesMissed;
					this.passesReceived = passesReceived;
					this.tacklings = tacklings;
					this.tacklesWon = tacklesWon;
					this.goalsScored = goalsScored;
					this.ballContacts = ballContacts;
					this.possessionTime = possessionTime;
				}
			},
			CurrentPositionData : function CurrentPositionData(positionNodes) {
				if (arguments[0] instanceof jQuery) {
					var _CurrentPositionData = this;
					_CurrentPositionData.positionNodes = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "BallPosition": _CurrentPositionData.positionNodes.push(new sea.pubsub.ELEMENTS.BallPosition($(this))); break;
							case "PlayerPosition": _CurrentPositionData.positionNodes.push(new sea.pubsub.ELEMENTS.PlayerPosition($(this))); break;
						}
					});
				} else {
					this.positionNodes = positionNodes;
				}
			},
			CurrentPlayerData : function CurrentPlayerData(playerStatisticNodes) {
				if (arguments[0] instanceof jQuery) {
					var _CurrentPlayerData = this;
					_CurrentPlayerData.playerStatisticNodes = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "PlayerStatistic": _CurrentPlayerData.playerStatisticNodes.push(new sea.pubsub.ELEMENTS.PlayerStatistic($(this))); break;
						}
					});
				} else {
					this.playerStatisticNodes = playerStatisticNodes;
				}
			}
		},

		DECORATORS : {
			CurrentPositionDataHandler : function(_callback, _return) {
				return function(_iq) {
					var $iq = $(_iq).children("event").children("items");
					
					$.each($iq.children("item"), function(index, value) {
						_callback.apply(this, [new sea.pubsub.ELEMENTS.CurrentPositionData($(value).children())]);
					});

					return _return;
				}
			},
			CurrentPlayerDataHandler : function(_callback, _return) {
				return function(_iq) {
					var $iq = $(_iq).children("event").children("items");
					
					$.each($iq.children("item"), function(index, value) {
						_callback.apply(this, [new sea.pubsub.ELEMENTS.CurrentPlayerData($(value).children())]);
					});

					return _return;
				}
			}
		},

		subscribeCurrentPositionData : function(onEvent) {
			Mobilis.connection.pubsub.subscribe("CurrentPositionData", [], sea.pubsub.DECORATORS.CurrentPositionDataHandler(onEvent, true), function(iq) {
				// Result: Because of XEP-0060 mandatory but without useful informations (Just like: "Dude, you are registered!")
				return false;
			}, function(iq) {
				// Errorcallback
				Mobilis.utils.trace("(PubSub) CurrentPositionData", iq);
			});
		},

		subscribeCurrentPlayerData : function(onEvent) {
			Mobilis.connection.pubsub.subscribe("CurrentPlayerData", [], sea.pubsub.DECORATORS.CurrentPlayerDataHandler(onEvent, true), function(iq) {
				// Result: Because of XEP-0060 mandatory but without useful informations (Just like: "Dude, you are registered!")
				return false;
			}, function(iq) {
				// Errorcallback
				Mobilis.utils.trace("(PubSub) CurrentPlayerData", iq);
			});
		}

	}

})();
