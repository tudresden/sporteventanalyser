package de.tudresden.inf.rn.mobilis.sea.jingle.connection.manager.observer;

import de.tudresden.inf.rn.mobilis.sea.jingle.connection.media.Raw;

/**
 * A <code>ReceptionListener</code> may be created to handle <code>Raw</code>
 * items (the item has to be cast to the corresponding type by yourself)
 */
public interface ReceptionListener {

	/**
	 * Handle the incoming item
	 * 
	 * @param item
	 *            the <code>Raw</code> item which may be used to do something
	 *            special
	 */
	public void handle(Raw item);

}
