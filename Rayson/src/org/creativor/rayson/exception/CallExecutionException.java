/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.exception;

import org.creativor.rayson.client.ClientCall;

/**
 *
 * @author Nick Zhang
 */
/**
 * Exception thrown when attempting to retrieve the result of a
 * {@link ClientCall} that aborted by throwing an exception. This exception can
 * be inspected using the {@link #getCause()} method.
 * 
 */
public final class CallExecutionException extends Exception {

	/**
	 */
	private static final long serialVersionUID = 1L;

	public CallExecutionException(Throwable cause) {
		super(cause);
	}

	@Override
	public Throwable getCause() {
		return super.getCause();
	}
}