/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.creativor.rayson.exception;

import java.lang.reflect.Method;

/**
 * If one rpc proxy method is illegal one.
 * 
 * @author Nick Zhang
 */
public class IllegalProxyMethodException extends Exception {

	private Method method;

	public IllegalProxyMethodException(Method method, String message) {
		super(method.toString() + " is illegal: " + message);
		this.method = method;
	}

	/**
	 * @return the illegal proxy method.
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 */
	private static final long serialVersionUID = 1L;

}
