package org.rayson.api;

public class ServiceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceNotFoundException(String message) {
		super(message);
	}
}
