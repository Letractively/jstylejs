/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.exception;

public class RpcExcptionInstantiationException extends Exception {

	private static final long serialVersionUID = 1L;

	public RpcExcptionInstantiationException(Throwable cause) {
		super(cause);
	}

	@Override
	public Throwable getCause() {
		return super.getCause();
	}

}
