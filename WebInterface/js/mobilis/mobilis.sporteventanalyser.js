(function() {

	var sporteventanalyser = {

		NS : {
			SERVICE : "http://mobilis.inf.tu-dresden.de#services/SportEventAnalyserService",
			PLAYERMAPPINGS : "sea:iq:playermappings"
		},

		ELEMENTS : {
			Mapping : function Mapping(PlayerID, PlayerName, TeamID) {
				if (arguments[0] instanceof jQuery) {
					var _Mapping = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "PlayerID": _Mapping.PlayerID = $(this).text(); break;
							case "PlayerName": _Mapping.PlayerName = $(this).text(); break;
							case "TeamID": _Mapping.TeamID = $(this).text(); break;
						}
					});
				} else {
					this.PlayerID = PlayerID;
					this.PlayerName = PlayerName;
					this.TeamID = TeamID;
				}
			},
			Mappings : function Mappings(Mappings) {
				if (arguments[0] instanceof jQuery) {
					var _Mappings = this;
					_Mappings.Mappings = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "Mapping": _Mappings.Mappings.push(new Mobilis.sporteventanalyser.ELEMENTS.Mapping($(this))); break;
						}
					});
				} else {
					this.Mappings = Mappings;
				}
			},
			MappingRequest : function MappingRequest() {
			}
		},

		DECORATORS : {
			PlayerMappingsHandler : function(_callback, _return) {
				return function(_iq) {
					var $iq = $(_iq);

					_callback.apply(this, [new Mobilis.sporteventanalyser.ELEMENTS.Mappings($iq.children())]);

					return _return;
				};
			}
		},

		PlayerMappings : function(MappingRequest, onResult, onTimeout) {
			var _iq = Mobilis.utils.createMobilisServiceIq(Mobilis.sporteventanalyser.NS.SERVICE, {
				type : "get"
			});
			_iq.c("PlayerMappings", {
				xmlns : Mobilis.sporteventanalyser.NS.PLAYERMAPPINGS
			});
			Mobilis.utils.appendElement(_iq, MappingRequest);
			Mobilis.core.sendIQ(_iq, onResult ? Mobilis.sporteventanalyser.DECORATORS.PlayerMappingsHandler(onResult, false) : null, null, onTimeout);
		},

		addPlayerMappingsHandler : function(handler) {
			Mobilis.core.addHandler(Mobilis.sporteventanalyser.DECORATORS.PlayerMappingsHandler(handler, true), Mobilis.sporteventanalyser.NS.PLAYERMAPPINGS, "result");
		}
	}

	Mobilis.extend("sporteventanalyser", sporteventanalyser);

})();