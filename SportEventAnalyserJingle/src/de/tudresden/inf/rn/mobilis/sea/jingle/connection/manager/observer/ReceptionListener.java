package de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;

public interface ReceptionListener {

	public void handle(Raw item);

}
