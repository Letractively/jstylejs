package org.rayson.api;

import java.io.IOException;

public class NetWorkException extends Exception {

	public NetWorkException(IOException cause) {
		super(cause);
	}

	/**
	 * Get {@link IOException} that caused this net work exception.
	 */
	@Override
	public IOException getCause() {
		return (IOException) super.getCause();
	}

	private static final long serialVersionUID = 1L;

}
