/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.conf;

public class LoadConfigException extends Exception {

	/**
	 */
	private static final long serialVersionUID = 1L;

	public LoadConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoadConfigException(String message) {
		super(message);
	}

	public LoadConfigException(Throwable cause) {
		super(cause);
	}

}
