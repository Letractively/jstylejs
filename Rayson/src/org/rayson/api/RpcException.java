package org.rayson.api;


public class RpcException extends Exception {
	private static final long serialVersionUID = 1L;
	private Throwable cause;

	public RpcException(Throwable cause) {
		super(cause);
	}

	@Override
	public Throwable getCause() {
		return super.getCause();
	}
}
