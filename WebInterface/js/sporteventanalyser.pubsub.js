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
							case "id": _PlayerPosition.id = $(this).text(); break;
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
							case "id": _PlayerPosition.id = $(this).text(); break;
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
			TeamStatistic : function TeamStatistic(id, ballPossession, passingAccuracy) {
				if (arguments[0] instanceof jQuery) {
					var _TeamStatistic = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "teamname": _TeamStatistic.id = $(this).text(); break;
							case "ballPossession": _TeamStatistic.ballPossession = $(this).text(); break;
							case "passingAccuracy": _TeamStatistic.passingAccuracy = $(this).text(); break;
						}
					});
				} else {
					this.id = id;
					this.ballPossession = ballPossession;
					this.passingAccuracy = passingAccuracy;
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
			CurrentPlayerData : function CurrentPlayerData(playerStatistics) {
				if (arguments[0] instanceof jQuery) {
					var _CurrentPlayerData = this;
					_CurrentPlayerData.playerStatistics = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "PlayerStatistic": _CurrentPlayerData.playerStatistics.push(new sea.pubsub.ELEMENTS.PlayerStatistic($(this))); break;
						}
					});
				} else {
					this.playerStatistics = playerStatistics;
				}
			},
			CurrentTeamData : function CurrentTeamData(teamStatistics) {
				if (arguments[0] instanceof jQuery) {
					var _CurrentTeamData = this;
					_CurrentTeamData.teamStatistics = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "TeamStatistic": _CurrentTeamData.teamStatistics.push(new sea.pubsub.ELEMENTS.TeamStatistic($(this))); break;
						}
					});
				} else {
					this.teamStatistics = teamStatistics;
				}
			}
		},

		INTERNAL : {
			handlers : [],
			Handler : function(handler, node) {
				this.handle = handler;
				this.match = function(name) {
					return node == name;
				}
			},
			addHandler : function(handler, node) {
				var hand = new sea.pubsub.INTERNAL.Handler(handler, node);
				sea.pubsub.INTERNAL.handlers.push(hand);
				return hand;
			},
			ItemDistributer : function(_iq) {
				var $iq = $(_iq).children("event").children("items");

				var $item, node;
				$.each($iq.children("item"), function(index, value) {
					node = ($item = $(value)).attr("node");
					for (var i = 0; i < sea.pubsub.INTERNAL.handlers.length; i++) {
						if (sea.pubsub.INTERNAL.handlers[i].match(node)) {
							sea.pubsub.INTERNAL.handlers[i].handle($item);
						}
					}
				});

				return true;
			}
		},

		DECORATORS : {
			CurrentPositionDataHandler : function(_callback) {
				return function($item) {
					_callback.apply(this, [new sea.pubsub.ELEMENTS.CurrentPositionData($item.children())]);
				}
			},
			CurrentPlayerDataHandler : function(_callback) {
				return function($item) {
					_callback.apply(this, [new sea.pubsub.ELEMENTS.CurrentPlayerData($item.children())]);
				}
			},
			CurrentTeamDataHandler : function(_callback) {
				return function($item) {
					_callback.apply(this, [new sea.pubsub.ELEMENTS.CurrentTeamData($item.children())]);
				}
			}
		},

		subscribeStatistic : function() {
			Mobilis.connection.pubsub.subscribe("Statistic", { "pubsub#subscription_type" : "items",
				"pubsub#subscription_depth" : "all"}, sea.pubsub.INTERNAL.ItemDistributer, function(iq) {
				// Result: Because of XEP-0060 mandatory but without useful informations (Just like: "Dude, you are registered!")
				return false;
			}, function(iq) {
				// Errorcallback
				Mobilis.utils.trace("PubSub - Error on subscribing", iq);
			});
		},

		addCurrentPositionDataHandler : function(onEvent) {
			sea.pubsub.INTERNAL.addHandler(sea.pubsub.DECORATORS.CurrentPositionDataHandler(onEvent), "CurrentPositionData");
		},

		addCurrentPlayerDataHandler : function(onEvent) {
			sea.pubsub.INTERNAL.addHandler(sea.pubsub.DECORATORS.CurrentPlayerDataHandler(onEvent), "CurrentPlayerData");
		},

		addCurrentTeamDataHandler : function(onEvent) {
			sea.pubsub.INTERNAL.addHandler(sea.pubsub.DECORATORS.CurrentTeamDataHandler(onEvent), "CurrentTeamData");
		}

	}

})();
