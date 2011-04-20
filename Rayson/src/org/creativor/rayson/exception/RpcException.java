/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.exception;

import java.lang.reflect.UndeclaredThrowableException;

/**
 *
 * @author Nick Zhang
 */
public abstract class RpcException extends Exception {

	private static final long serialVersionUID = 1L;

	private Throwable cause;

	private RemoteExceptionType type = RemoteExceptionType.UNDECLARED;

	protected RpcException(RemoteExceptionType type, Throwable cause) {
		this.type = type;
		this.cause = cause;
	}

	/**
	 * Get exception that cause this remote exception.
	 */
	@Override
	public Throwable getCause() {
		return super.getCause();
	}

	public RemoteExceptionType getType() {
		return type;
	}

	/**
	 * Throw the cause exception of this remote exception.
	 * 
	 * @throws ReadInvocationException
	 *             If read the call parameter from server or client error.
	 * @throws NetWorkException
	 *             If network exception occurred.
	 * @throws ServiceNotFoundException
	 *             If remote RPC service not found.
	 * @throws UndeclaredThrowableException
	 *             If undeclared exception occurred in remote server.
	 * @throws UnsupportedVersionException
	 *             If the local RPC proxy version is unsupported by remote
	 *             server.
	 */
	public void throwCause() throws NetWorkException, ServiceNotFoundException,
			ReadInvocationException, UndeclaredThrowableException,
			UnsupportedVersionException {
		switch (type) {

		case CALL:
			throw (ReadInvocationException) cause;
		case NETWORK:
			throw (NetWorkException) cause;

		case SERVICE_NOT_FOUND:
			throw (ServiceNotFoundException) cause;

		case UNSUPPORTED_PROXY_VERSION:
			throw (UnsupportedVersionException) cause;

		case UNDECLARED:
			throw new UndeclaredThrowableException(cause);

		default:
			throw new UndeclaredThrowableException(cause);
		}

	}

	@Override
	public String toString() {
		// UndeclaredThrowableException
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("type: ");
		sb.append(type.name());
		sb.append(", exception: ");
		sb.append(this.cause.toString());
		sb.append("}");
		return sb.toString();
	}
}
