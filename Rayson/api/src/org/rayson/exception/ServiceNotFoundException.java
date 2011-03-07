package org.rayson.exception;

public final class ServiceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceNotFoundException(String message) {
		super(message);
	}
}
