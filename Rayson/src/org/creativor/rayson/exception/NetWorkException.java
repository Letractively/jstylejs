/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.exception;

import java.io.IOException;

public class NetWorkException extends Exception {

	private static final long serialVersionUID = 1L;

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

}
