(function() {

	var xslttest = {

		NS : {
			SERVICE : "http://mobilis.inf.tu-dresden.de#services/XSLTTestService",
			INONLYOPERATION : "xsltt:iq:inonly",
			INONLYOPERATIONWITHFAULT : "xsltt:iq:inonlywithfault",
			OUTONLYOPERATION : "xsltt:iq:outonly",
			OUTONLYOPERATIONWITHFAULT : "xsltt:iq:outonlywithfault",
			OUTINOPERATIONWITHFAULT : "xsltt:iq:outinwithfault",
			INOUTOPERATIONWITHFAULT : "xsltt:iq:inoutwithfault"
		},

		ELEMENTS : {
			OutComplexXSSequence : function OutComplexXSSequence(outSeq) {
				if (arguments[0] instanceof jQuery) {
					var _OutComplexXSSequence = this;
					_OutComplexXSSequence.outSeq = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "outSeq": _OutComplexXSSequence.outSeq.push($(this).text()); break;
						}
					});
				} else {
					this.outSeq = outSeq;
				}
			},
			InComplexXSSequence : function InComplexXSSequence(inSeq) {
				if (arguments[0] instanceof jQuery) {
					var _InComplexXSSequence = this;
					_InComplexXSSequence.inSeq = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "inSeq": _InComplexXSSequence.inSeq.push($(this).text()); break;
						}
					});
				} else {
					this.inSeq = inSeq;
				}
			},
			OutComplexXS : function OutComplexXS(outVal) {
				if (arguments[0] instanceof jQuery) {
					var _OutComplexXS = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "outVal": _OutComplexXS.outVal = $(this).text(); break;
						}
					});
				} else {
					this.outVal = outVal;
				}
			},
			InComplexXS : function InComplexXS(inVal) {
				if (arguments[0] instanceof jQuery) {
					var _InComplexXS = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "inVal": _InComplexXS.inVal = $(this).text(); break;
						}
					});
				} else {
					this.inVal = inVal;
				}
			},
			OutComplexFull : function OutComplexFull(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, outVal) {
				if (arguments[0] instanceof jQuery) {
					var _OutComplexFull = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "intVal": _OutComplexFull.intVal = $(this).text(); break;
							case "longVal": _OutComplexFull.longVal = $(this).text(); break;
							case "booleanVal": _OutComplexFull.booleanVal = $(this).text(); break;
							case "doubleVal": _OutComplexFull.doubleVal = $(this).text(); break;
							case "floatVal": _OutComplexFull.floatVal = $(this).text(); break;
							case "byteVal": _OutComplexFull.byteVal = $(this).text(); break;
							case "shortVal": _OutComplexFull.shortVal = $(this).text(); break;
							case "stringVal": _OutComplexFull.stringVal = $(this).text(); break;
							case "OutComplexXS": _OutComplexFull.outVal = new Mobilis.xslttest.ELEMENTS.OutComplexXS($(this)); break;
						}
					});
				} else {
					this.intVal = intVal;
					this.longVal = longVal;
					this.booleanVal = booleanVal;
					this.doubleVal = doubleVal;
					this.floatVal = floatVal;
					this.byteVal = byteVal;
					this.shortVal = shortVal;
					this.stringVal = stringVal;
					this.outVal = outVal;
				}
			},
			InComplexFull : function InComplexFull(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, inVal) {
				if (arguments[0] instanceof jQuery) {
					var _InComplexFull = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "intVal": _InComplexFull.intVal = $(this).text(); break;
							case "longVal": _InComplexFull.longVal = $(this).text(); break;
							case "booleanVal": _InComplexFull.booleanVal = $(this).text(); break;
							case "doubleVal": _InComplexFull.doubleVal = $(this).text(); break;
							case "floatVal": _InComplexFull.floatVal = $(this).text(); break;
							case "byteVal": _InComplexFull.byteVal = $(this).text(); break;
							case "shortVal": _InComplexFull.shortVal = $(this).text(); break;
							case "stringVal": _InComplexFull.stringVal = $(this).text(); break;
							case "InComplexXS": _InComplexFull.inVal = new Mobilis.xslttest.ELEMENTS.InComplexXS($(this)); break;
						}
					});
				} else {
					this.intVal = intVal;
					this.longVal = longVal;
					this.booleanVal = booleanVal;
					this.doubleVal = doubleVal;
					this.floatVal = floatVal;
					this.byteVal = byteVal;
					this.shortVal = shortVal;
					this.stringVal = stringVal;
					this.inVal = inVal;
				}
			},
			InElement : function InElement() {
			},
			InElementFull : function InElementFull(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, inComplexSeq) {
				if (arguments[0] instanceof jQuery) {
					var _InElementFull = this;
					_InElementFull.inComplexSeq = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "intVal": _InElementFull.intVal = $(this).text(); break;
							case "longVal": _InElementFull.longVal = $(this).text(); break;
							case "booleanVal": _InElementFull.booleanVal = $(this).text(); break;
							case "doubleVal": _InElementFull.doubleVal = $(this).text(); break;
							case "floatVal": _InElementFull.floatVal = $(this).text(); break;
							case "byteVal": _InElementFull.byteVal = $(this).text(); break;
							case "shortVal": _InElementFull.shortVal = $(this).text(); break;
							case "stringVal": _InElementFull.stringVal = $(this).text(); break;
							case "InComplexXSSequence": _InElementFull.inComplexSeq.push(new Mobilis.xslttest.ELEMENTS.InComplexXSSequence($(this))); break;
						}
					});
				} else {
					this.intVal = intVal;
					this.longVal = longVal;
					this.booleanVal = booleanVal;
					this.doubleVal = doubleVal;
					this.floatVal = floatVal;
					this.byteVal = byteVal;
					this.shortVal = shortVal;
					this.stringVal = stringVal;
					this.inComplexSeq = inComplexSeq;
				}
			},
			OutElement : function OutElement() {
			},
			OutElementFull : function OutElementFull(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, outComplexSeq) {
				if (arguments[0] instanceof jQuery) {
					var _OutElementFull = this;
					_OutElementFull.outComplexSeq = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "intVal": _OutElementFull.intVal = $(this).text(); break;
							case "longVal": _OutElementFull.longVal = $(this).text(); break;
							case "booleanVal": _OutElementFull.booleanVal = $(this).text(); break;
							case "doubleVal": _OutElementFull.doubleVal = $(this).text(); break;
							case "floatVal": _OutElementFull.floatVal = $(this).text(); break;
							case "byteVal": _OutElementFull.byteVal = $(this).text(); break;
							case "shortVal": _OutElementFull.shortVal = $(this).text(); break;
							case "stringVal": _OutElementFull.stringVal = $(this).text(); break;
							case "OutComplexXSSequence": _OutElementFull.outComplexSeq.push(new Mobilis.xslttest.ELEMENTS.OutComplexXSSequence($(this))); break;
						}
					});

					var _pi = arguments[1], _on = arguments[2], _xmlns = arguments[3];
					this.sendInFaultSimple = function(_message) {
						var _iq = Mobilis.utils.createMobilisServiceIq(Mobilis.xslttest.NS.SERVICE, {
							id : _pi,
							type : "error"
						});
						_iq.c(_on, {
							xmlns : _xmlns
						});
						Mobilis.utils.appendElement(_iq, _OutElementFull);
						_iq.node = _iq.node.childNodes[0];

						_iq.c("error", {
							type : "modify"
						}).c("bad-request", {
							xmlns : Mobilis.core.NS.NAMESPACE_ERROR_STANZA
						}).up().c("text", {
							xmlns : Mobilis.core.NS.NAMESPACE_ERROR_STANZA
						}).t("Fault incoming" + (_message ? ": " + _message : ""));
						Mobilis.core.sendIQ(_iq);
					}
				} else {
					this.intVal = intVal;
					this.longVal = longVal;
					this.booleanVal = booleanVal;
					this.doubleVal = doubleVal;
					this.floatVal = floatVal;
					this.byteVal = byteVal;
					this.shortVal = shortVal;
					this.stringVal = stringVal;
					this.outComplexSeq = outComplexSeq;
				}
			},
			OutElementXS : function OutElementXS(outVal) {
				if (arguments[0] instanceof jQuery) {
					var _OutElementXS = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "outVal": _OutElementXS.outVal = $(this).text(); break;
						}
					});

					var _pi = arguments[1], _on = arguments[2], _xmlns = arguments[3];
					this.sendInFaultSimple = function(_message) {
						var _iq = Mobilis.utils.createMobilisServiceIq(Mobilis.xslttest.NS.SERVICE, {
							id : _pi,
							type : "error"
						});
						_iq.c(_on, {
							xmlns : _xmlns
						});
						Mobilis.utils.appendElement(_iq, _OutElementXS);
						_iq.node = _iq.node.childNodes[0];

						_iq.c("error", {
							type : "modify"
						}).c("bad-request", {
							xmlns : Mobilis.core.NS.NAMESPACE_ERROR_STANZA
						}).up().c("text", {
							xmlns : Mobilis.core.NS.NAMESPACE_ERROR_STANZA
						}).t("Fault incoming" + (_message ? ": " + _message : ""));
						Mobilis.core.sendIQ(_iq);
					}
				} else {
					this.outVal = outVal;
				}
			},
			InElementXS : function InElementXS(inVal) {
				if (arguments[0] instanceof jQuery) {
					var _InElementXS = this;

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "inVal": _InElementXS.inVal = $(this).text(); break;
						}
					});
				} else {
					this.inVal = inVal;
				}
			},
			OutElementXSSequence : function OutElementXSSequence(outSeq) {
				if (arguments[0] instanceof jQuery) {
					var _OutElementXSSequence = this;
					_OutElementXSSequence.outSeq = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "outSeq": _OutElementXSSequence.outSeq.push($(this).text()); break;
						}
					});
				} else {
					this.outSeq = outSeq;
				}
			},
			InElementXSSequence : function InElementXSSequence(inSeq) {
				if (arguments[0] instanceof jQuery) {
					var _InElementXSSequence = this;
					_InElementXSSequence.inSeq = [];

					arguments[0].children().each(function() {
						switch($(this).prop("tagName")) {
							case "inSeq": _InElementXSSequence.inSeq.push($(this).text()); break;
						}
					});
				} else {
					this.inSeq = inSeq;
				}
			}
		},

		DECORATORS : {
			OutOnlyOperationHandler : function(_callback, _return) {
				return function(_iq) {
					var $iq = $(_iq);

					_callback.apply(this, [new Mobilis.xslttest.ELEMENTS.OutElement($iq.children())]);

					return _return;
				};
			},
			OutOnlyOperationWithFaultHandler : function(_callback, _return) {
				return function(_iq) {
					var $iq = $(_iq);

					_callback.apply(this, [new Mobilis.xslttest.ELEMENTS.OutElementFull($iq.children(), $iq.attr("id"), "OutOnlyOperationWithFault", Mobilis.xslttest.NS.OUTONLYOPERATIONWITHFAULT)]);

					return _return;
				};
			},
			OutInOperationWithFaultHandler : function(_callback, _return) {
				return function(_iq) {
					var $iq = $(_iq);

					_callback.apply(this, [new Mobilis.xslttest.ELEMENTS.OutElementXS($iq.children(), $iq.attr("id"), "OutInOperationWithFault", Mobilis.xslttest.NS.OUTINOPERATIONWITHFAULT), $iq.attr("id")]);

					return _return;
				};
			},
			InOutOperationWithFaultHandler : function(_callback, _return) {
				return function(_iq) {
					var $iq = $(_iq);

					_callback.apply(this, [new Mobilis.xslttest.ELEMENTS.OutElementXSSequence($iq.children())]);

					return _return;
				};
			},
			InOnlyOperationWithFaultFaultHandler : function(_callback) {
				return function(_iq) {
					var $iq = $(_iq).children();
					var $error = $iq.children("error");

					_callback.apply(this, [new Mobilis.xslttest.ELEMENTS.InElementFull($iq), {
						type : $error.attr("type"),
						condition : $error.children().prop("tagName"),
						message : $error.find("text").text()
					}]);
				}
			},
			InOutOperationWithFaultFaultHandler : function(_callback) {
				return function(_iq) {
					var $iq = $(_iq).children();
					var $error = $iq.children("error");

					_callback.apply(this, [new Mobilis.xslttest.ELEMENTS.InElementXSSequence($iq), {
						type : $error.attr("type"),
						condition : $error.children().prop("tagName"),
						message : $error.find("text").text()
					}]);
				}
			}
		},

		InOnlyOperation : function(InElement) {
			var _iq = Mobilis.utils.createMobilisServiceIq(Mobilis.xslttest.NS.SERVICE, {
				type : "set"
			});
			_iq.c("InOnlyOperation", {
				xmlns : Mobilis.xslttest.NS.INONLYOPERATION
			});
			Mobilis.utils.appendElement(_iq, InElement);
			Mobilis.core.sendIQ(_iq, null, null);
		},

		InOnlyOperationWithFault : function(InElementFull, onError) {
			var _iq = Mobilis.utils.createMobilisServiceIq(Mobilis.xslttest.NS.SERVICE, {
				type : "set"
			});
			_iq.c("InOnlyOperationWithFault", {
				xmlns : Mobilis.xslttest.NS.INONLYOPERATIONWITHFAULT
			});
			Mobilis.utils.appendElement(_iq, InElementFull);
			Mobilis.core.sendIQ(_iq, null, Mobilis.xslttest.DECORATORS.InOnlyOperationWithFaultFaultHandler(onError));
		},

		OutInOperationWithFault : function(InElementXS, packetID) {
			var _iq = Mobilis.utils.createMobilisServiceIq(Mobilis.xslttest.NS.SERVICE, {
				id : packetID,
				type : "result"
			});
			_iq.c("OutInOperationWithFault", {
				xmlns : Mobilis.xslttest.NS.OUTINOPERATIONWITHFAULT
			});
			Mobilis.utils.appendElement(_iq, InElementXS);
			Mobilis.core.sendIQ(_iq, null, null);
		},

		InOutOperationWithFault : function(InElementXSSequence, onResult, onError, onTimeout) {
			var _iq = Mobilis.utils.createMobilisServiceIq(Mobilis.xslttest.NS.SERVICE, {
				type : "get"
			});
			_iq.c("InOutOperationWithFault", {
				xmlns : Mobilis.xslttest.NS.INOUTOPERATIONWITHFAULT
			});
			Mobilis.utils.appendElement(_iq, InElementXSSequence);
			Mobilis.core.sendIQ(_iq, Mobilis.xslttest.DECORATORS.InOutOperationWithFaultHandler(onResult, false), Mobilis.xslttest.DECORATORS.InOutOperationWithFaultFaultHandler(onError), onTimeout);
		},

		addOutOnlyOperationHandler : function(handler) {
			Mobilis.core.addHandler(Mobilis.xslttest.DECORATORS.OutOnlyOperationHandler(handler, true), Mobilis.xslttest.NS.OUTONLYOPERATION, "chat");
		},

		addOutOnlyOperationWithFaultHandler : function(handler) {
			Mobilis.core.addHandler(Mobilis.xslttest.DECORATORS.OutOnlyOperationWithFaultHandler(handler, true), Mobilis.xslttest.NS.OUTONLYOPERATIONWITHFAULT, "set");
		},

		addOutInOperationWithFaultHandler : function(handler) {
			Mobilis.core.addHandler(Mobilis.xslttest.DECORATORS.OutInOperationWithFaultHandler(handler, true), Mobilis.xslttest.NS.OUTINOPERATIONWITHFAULT, "get");
		},

		addInOutOperationWithFaultHandler : function(handler) {
			Mobilis.core.addHandler(Mobilis.xslttest.DECORATORS.InOutOperationWithFaultHandler(handler, true), Mobilis.xslttest.NS.INOUTOPERATIONWITHFAULT, "result");
		}
	}

	Mobilis.extend("xslttest", xslttest);

})();