package org.rayson.server;

class ServiceExistedException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceExistedException(String serviceName) {
		super(serviceName + " already existed");
	}

}
