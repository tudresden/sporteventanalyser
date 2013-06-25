(function() {

	var sporteventanalyser = {

		NS : {
			SERVICE : "http://mobilis.inf.tu-dresden.de#services/SportEventAnalyserService",
			GAMEMAPPINGS : "sea:iq:gamemappings"
		},

		ELEMENTS : {
			Mapping : function Mapping(PlayerID, PlayerName, TeamName) {
				if (arguments[0] instanceof jQuery) {
					var _Mapping = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "PlayerID": _Mapping.PlayerID = $(this).text(); break;
							case "PlayerName": _Mapping.PlayerName = $(this).text(); break;
							case "TeamName": _Mapping.TeamName = $(this).text(); break;
						}
					});
				} else {
					this.PlayerID = PlayerID;
					this.PlayerName = PlayerName;
					this.TeamName = TeamName;
				}
			},
			GameField : function GameField(GameFieldMinX, GameFieldMaxX, GameFieldMinY, GameFieldMaxY) {
				if (arguments[0] instanceof jQuery) {
					var _GameField = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "GameFieldMinX": _GameField.GameFieldMinX = $(this).text(); break;
							case "GameFieldMaxX": _GameField.GameFieldMaxX = $(this).text(); break;
							case "GameFieldMinY": _GameField.GameFieldMinY = $(this).text(); break;
							case "GameFieldMaxY": _GameField.GameFieldMaxY = $(this).text(); break;
						}
					});
				} else {
					this.GameFieldMinX = GameFieldMinX;
					this.GameFieldMaxX = GameFieldMaxX;
					this.GameFieldMinY = GameFieldMinY;
					this.GameFieldMaxY = GameFieldMaxY;
				}
			},
			Goal : function Goal(GoalMinX, GoalMaxX) {
				if (arguments[0] instanceof jQuery) {
					var _Goal = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "GoalMinX": _Goal.GoalMinX = $(this).text(); break;
							case "GoalMaxX": _Goal.GoalMaxX = $(this).text(); break;
						}
					});
				} else {
					this.GoalMinX = GoalMinX;
					this.GoalMaxX = GoalMaxX;
				}
			},
			Mappings : function Mappings(GameFieldSize, Goals, PlayerMappings) {
				if (arguments[0] instanceof jQuery) {
					var _Mappings = this;
					_Mappings.Goals = [];
					_Mappings.PlayerMappings = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "GameField": _Mappings.GameFieldSize = new Mobilis.sporteventanalyser.ELEMENTS.GameField($(this)); break;
							case "Goal": _Mappings.Goals.push(new Mobilis.sporteventanalyser.ELEMENTS.Goal($(this))); break;
							case "Mapping": _Mappings.PlayerMappings.push(new Mobilis.sporteventanalyser.ELEMENTS.Mapping($(this))); break;
						}
					});
				} else {
					this.GameFieldSize = GameFieldSize;
					this.Goals = Goals;
					this.PlayerMappings = PlayerMappings;
				}
			},
			MappingRequest : function MappingRequest() {
			}
		},

		DECORATORS : {
			GameMappingsHandler : function(_callback, _return) {
				return function(_iq) {
					var $iq = $(_iq);

					_callback.apply(this, [new Mobilis.sporteventanalyser.ELEMENTS.Mappings($iq.children())]);

					return _return;
				};
			}
		},

		GameMappings : function(MappingRequest, onResult, onTimeout) {
			var _iq = Mobilis.utils.createMobilisServiceIq(Mobilis.sporteventanalyser.NS.SERVICE, {
				type : "get"
			});
			_iq.c("GameMappings", {
				xmlns : Mobilis.sporteventanalyser.NS.GAMEMAPPINGS
			});
			Mobilis.utils.appendElement(_iq, MappingRequest);
			Mobilis.core.sendIQ(_iq, onResult ? Mobilis.sporteventanalyser.DECORATORS.GameMappingsHandler(onResult, false) : null, null, onTimeout);
		},

		addGameMappingsHandler : function(handler) {
			Mobilis.core.addHandler(Mobilis.sporteventanalyser.DECORATORS.GameMappingsHandler(handler, true), Mobilis.sporteventanalyser.NS.GAMEMAPPINGS, "result");
		}
	}

	Mobilis.extend("sporteventanalyser", sporteventanalyser);

})();