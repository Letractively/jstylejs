package org.creativor.rayson.common;

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
