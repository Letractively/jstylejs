package org.creativor.rayson.transport.api;

public class ServiceAlreadyExistedException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceAlreadyExistedException(String serviceName) {
		super(serviceName + " already existed");
	}

}
