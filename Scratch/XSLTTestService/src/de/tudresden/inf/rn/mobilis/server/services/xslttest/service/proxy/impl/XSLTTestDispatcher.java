package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.impl;

import org.jivesoftware.smack.Connection;

import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.Event;
import de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy.IXSLTTestIncoming;

public class XSLTTestDispatcher implements IXSLTTestIncoming {

	private Connection connection;

	public XSLTTestDispatcher(Connection connection) {
		this.connection = connection;
	}

}
