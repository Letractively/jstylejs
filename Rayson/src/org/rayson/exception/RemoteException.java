package org.rayson.exception;

import java.lang.reflect.UndeclaredThrowableException;

public abstract class RemoteException extends Exception {

	private static final long serialVersionUID = 1L;

	private Throwable cause;

	protected RemoteException(RemoteExceptionType type, Throwable cause) {
		this.type = type;
		this.cause = cause;
	}

	private RemoteExceptionType type = RemoteExceptionType.UNDECLARED;

	/**
	 * Throw the cause exception of this remote exception.
	 * 
	 * @throws NetWorkException
	 *             If network exception occurred.
	 * @throws ServiceNotFoundException
	 *             If remote RPC service not found.
	 * @throws UndeclaredThrowableException
	 *             If undeclared exception occurred in remote server.
	 */
	public void throwCause() throws NetWorkException, ServiceNotFoundException,
			UndeclaredThrowableException {
		switch (type) {
		case NETWORK:
			throw (NetWorkException) cause;

		case SERVICE_NOT_FOUND:
			throw (ServiceNotFoundException) cause;

		case UNDECLARED:
			throw new UndeclaredThrowableException(cause);

		default:
			throw new UndeclaredThrowableException(cause);
		}

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
