/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.client;

import java.io.IOException;

/**
 * Signals that the client connection to remote server was refuesed by remote
 * server
 */
class ConnectException extends IOException {

	private static final long serialVersionUID = 1L;

	public ConnectException(String message) {
		super(message);
	}

}
