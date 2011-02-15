package client;

import java.io.IOException;

/**
 * Signals that the client connection to remote server was refuesed by remote
 * server
 */
class ConnectException extends IOException {

	public ConnectException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
