/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.creativor.rayson.exception;

/**
 * If RPC call invoking got error in the remote server.
 * 
 * @author Nick Zhang
 */
public class CallInvokeException extends Exception {

	public CallInvokeException(Throwable cause) {
		super(cause);
	}

	@Override
	public Throwable getCause() {
		return super.getCause();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}