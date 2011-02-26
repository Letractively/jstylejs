package org.rayson.server;

class ServiceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceNotFoundException(String serviceName) {
		super(serviceName + " not found");
	}

}
