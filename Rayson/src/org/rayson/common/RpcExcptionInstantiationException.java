package org.rayson.common;

public class RpcExcptionInstantiationException extends Exception {

	public RpcExcptionInstantiationException(Throwable cause) {
		super(cause);
	}

	@Override
	public Throwable getCause() {
		return super.getCause();
	}

	private static final long serialVersionUID = 1L;

}
