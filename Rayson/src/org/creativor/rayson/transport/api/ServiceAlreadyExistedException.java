/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.api;

public class ServiceAlreadyExistedException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceAlreadyExistedException(String serviceName) {
		super(serviceName + " already existed");
	}

}
