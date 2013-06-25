(function() {

	var core = {

		/** Constants: Mobilis Services Namespaces
		 *
		 *  NS.COORDINATOR - Coordinator Service.
		 *  NS.ADMIN - Admin Service.
		 *  NS.DEPLOYMENT - Deployment Service.
		 */
		NS : {
			COORDINATOR : "http://mobilis.inf.tu-dresden.de#services/CoordinatorService",
			ADMIN : "http://mobilis.inf.tu-dresden.de#services/AdminService",
			DEPLOYMENT : "http://mobilis.inf.tu-dresden.de#services/DeploymentService",
			NAMESPACE_ERROR_STANZA : "urn:ietf:params:xml:ns:xmpp-stanzas"
		},

		/** Object: Services
		 *  Object containing Services objects referenced by their namespace.
		 */
		SERVICES : {
			"http://mobilis.inf.tu-dresden.de#services/CoordinatorService" : {
				version : "1.0",
				mode : "single",
				instances : "1",
				jid : null
			}
		},

		/** Object: EXCEPTIONS
		 *  Object containing some Exceptions which may be thrown
		 */
		EXCEPTIONS : {
			NotConnectedException : function NotConnectedException() {
				this.name = "NotConnectedException";
				this.message = "Mobilis is not connected to the XMPP-Server";
			},
			ServiceNotFoundException : function ServiceNotFoundException(service) {
				this.name = "NotFoundException";
				this.message = "Service could not been found: " + service;
			}
		},

		/** Function: connect
		 *  Establishes a connection to XMPP over BOSH and discovers services from the mobilis server
		 *
		 *  Parameters:
		 *    (String) uFullJid - Full JID of the user (e.g. user@mobilis/jsclient)
		 *    (String) uPassword - Password for this jid
		 *    (String) mBareJid - Bare JID of the server (e.g. server@mobilis)
		 *    (Function) onSuccess - Callback function when successfully connected
		 *    (Function) onError - Callback function when an error occured
		 */
		connect : function(uFullJid, uPassword, mBareJid, bind, onSuccess, onError) {
			// Set full jid of coordinator service
			Mobilis.core.SERVICES[Mobilis.core.NS.COORDINATOR].jid = mBareJid + "/Coordinator";
			onError = onError || Mobilis.utils.trace;

			var conn = new Strophe.Connection(bind);
			conn.connect(uFullJid, uPassword, function(status) {
				if (status == Strophe.Status.ERROR) {
					onError("Connection error");
				} else if (status == Strophe.Status.CONNECTING) {
					Mobilis.utils.trace("Connecting");
				} else if (status == Strophe.Status.CONNFAIL) {
					Mobilis.utils.trace("Connection failed");
				} else if (status == Strophe.Status.AUTHENTICATING) {
					Mobilis.utils.trace("Authenticating");
				} else if (status == Strophe.Status.AUTHFAIL) {
					onError("Authentication failed");
				} else if (status == Strophe.Status.CONNECTED) {
					Mobilis.utils.trace("Connected");
					conn.send($pres({
					}));
					Mobilis.core.mobilisServiceDiscovery(null, function(iq) {
						$(iq).find("mobilisService").each(function() {
							Mobilis.core.SERVICES[$(this).attr("namespace")] = {
								version : $(this).attr("version"),
								mode : $(this).attr("mode"),
								instances : $(this).attr("instances"),
								jid : $(this).attr("jid")
							};
						});
						Mobilis.utils.trace("Initial Service Discovery successful");
						onSuccess && onSuccess();
					}, function(iq) {
						onError("Initial Service Discovery failed", iq);
					}, 30000);
				} else if (status == Strophe.Status.DISCONNECTED) {
					Mobilis.utils.trace("Disconnected");
				} else if (status == Strophe.Status.DISCONNECTING) {
					Mobilis.utils.trace("Disconnecting");
				} else if (status == Strophe.Status.ATTACHED) {
					Mobilis.utils.trace("Connection has been attached");
				}
			});

			Mobilis.connection = conn;
		},

		/** Function: disconnect
		 *  Initiates a graceful teardown of the connection
		 *
		 *  Parameters:
		 *    (String) reason - reason for disconnection
		 */
		disconnect : function(reason) {
			Mobilis.utils.trace("Disconnect");
			Mobilis.connection.disconnect(reason);
		},

		/** Function: send
		 *  Send stanza that does not require acknowledgement, such as <msg> or <presence>
		 *
		 *  Parameters:
		 *    (XMLElement) elem - Stanza to send
		 *
		 *  Throws:
		 *    Throws a NotConnectedException if no connection has been established yet
		 */
		send : function(elem) {
			if (!Mobilis.connection)
				throw new Mobilis.core.EXCEPTIONS.NotConnectedException();

			Mobilis.connection.send(elem);
		},

		/** Function: sendIQ
		 *  Sends stanza that requires acknowledgement, such as <iq>. Callback functions are specified for the response stanzas
		 *
		 *  Parameters:
		 *    (XMLElement) elem - Stanza to send
		 *    (Function) onResult - Callback function for a successful request
		 *    (Function) onError - Callback for incoming response IQ stanzas of type ERROR
		 *    (Function) onTimeout - Callback which will be invoked when onResult timed out
		 *
		 *  Throws:
		 *    Throws a NotConnectedException if no connection has been established yet
		 */
		sendIQ : function(elem, onResult, onError, onTimeout) {
			if (!Mobilis.connection)
				throw new Mobilis.core.EXCEPTIONS.NotConnectedException();

			var callbackExpected = true;
			var errorReceived = false;
			if (!onResult) {
				callbackExpected = false;
				onResult = function(iq) {
					Mobilis.utils.trace("Callback received!", iq);
				}
			}
			Mobilis.connection.sendIQ(elem, function(iq) {
				callbackExpected = false;
				onResult(iq);
			}, function(iq) {
				if (iq) {
					if (onError)
						onError(iq);
					else
						Mobilis.utils.trace("Error received", iq);
				} else if (callbackExpected && !errorReceived) {
					if (onTimeout)
						onTimeout();
					else
						Mobilis.utils.trace("Callback timed out!");
				}
				errorReceived = true;
			}, 3000);
		},

		/** Function: createServiceInstance
		 *  Create Mobilis Service Instance
		 *
		 *  Parameters:
		 *    (Object) constraints - { serviceNamespace, serviceName, servicePassword }
		 *    (Function) onResult - Callback for incoming response IQ stanzas of type RESULT
		 *    (Function) onError - Callback for incoming response IQ stanzas of type ERROR, or timeout (Stanza will be null on timeout)
		 */
		createServiceInstance : function(constraints, onResult, onError) {
			var customiq = Mobilis.utils.createMobilisServiceIq(Mobilis.core.NS.COORDINATOR, {
				type : "set"
			}).c("createNewServiceInstance", {
				xmlns : Mobilis.core.NS.COORDINATOR
			});
			if (constraints) {
				$.each(constraints, function(k, v) {
					customiq.c(k).t(v).up();
				});
			}

			Mobilis.core.sendIQ(customiq, onResult, onError);
		},

		/** Function: mobilisServiceDiscovery
		 *  Performes a Mobilis Service discovery with the Mobilis Server
		 *
		 *  Parameters:
		 *    (Object) constraints - {serviceNamespace, serviceVersion}
		 *    (Function) onResult - Callback for incoming response IQ stanzas of type RESULT
		 *    (Function) onError - Callback for incoming response IQ stanzas of type ERROR, or timeout (Stanza will be null on timeout)
		 */
		mobilisServiceDiscovery : function(constraints, onResult, onError) {
			var discoiq = Mobilis.utils.createMobilisServiceIq(Mobilis.core.NS.COORDINATOR, {
				type : "get"
			}).c("serviceDiscovery", {
				xmlns : Mobilis.core.NS.COORDINATOR
			});
			if (constraints) {
				$.each(constraints, function(k, v) {
					discoiq.c(k).t(v).up();
				});
			}

			Mobilis.core.sendIQ(discoiq, onResult, onError);
		},

		/** Function: addHandler
		 *  Add a stanza handler for the connection. The handler callback will be called for any stanza
		 *  that matches the parameters.
		 *
		 *
		 *  Parameters:
		 *    (Function) handler - Handler callback function
		 *    (String) namespace - Stanza's namespace to match
		 *    (String) type - Type of IQ (get, set,...)
		 *
		 *  Throws:
		 *    Throws a NotConnectedException if no connection has been established yet
		 */
		addHandler : function(handler, namespace, type) {
			if (!Mobilis.connection)
				throw new Mobilis.core.EXCEPTIONS.NotConnectedException();

			Mobilis.connection.addHandler(handler, namespace, "iq", type);
		},

		/** Function: getFullJidFromNamespace
		 *  Returns the full jid of a service
		 *
		 *  Parameters:
		 *    (String) namespace - Namespace of the service
		 *
		 * 	Throws:
		 *    Throws a ServiceNotFoundException if the service is not found
		 */
		getFullJidFromNamespace : function(namespace) {
			if (!Mobilis.core.SERVICES[namespace])
				throw new Mobilis.core.EXCEPTIONS.ServiceNotFoundException(namespace);

			return Mobilis.core.SERVICES[namespace].jid;
		}

	};

	Mobilis.extend("core", core);

})();
		