package common;

/**
 * Signals that the client connection to remote server was refuesed by remote
 * server
 */
public class ConnectException extends Exception {

	public ConnectException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
