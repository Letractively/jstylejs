package org.rayson.api;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

public abstract class RemoteException extends Exception {

	private static final long serialVersionUID = 1L;

	private Throwable cause;

	protected RemoteException(Throwable cause) {
		this.cause = cause;
	}

	protected boolean isNetworkException = false;
	protected boolean isServiceNotFound = false;
	protected boolean isUndeclaredException = false;

	public boolean isNetworkException() {
		return isNetworkException;
	}

	/**
	 * Throw the cause exception of this remote exception.
	 * 
	 * @throws IOException
	 *             If network exception occurred.
	 * @throws ServiceNotFoundException
	 *             If remote RPC service not found.
	 * @throws UndeclaredThrowableException
	 *             If undeclared exception occurred in remote server.
	 */
	public void throwCause() throws IOException, ServiceNotFoundException,
			UndeclaredThrowableException {
		if (isNetworkException)
			throw (IOException) cause;
		if (isServiceNotFound)
			throw (ServiceNotFoundException) cause;
		throw new UndeclaredThrowableException(cause);
	}

	public boolean isServiceNotFound() {
		return isServiceNotFound;
	}

	public boolean isUndecleardException() {
		return isUndeclaredException;
	}

	@Override
	public String toString() {
		// UndeclaredThrowableException
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("type: ");
		String type = "NETWORK";
		if (isServiceNotFound)
			type = "SERVICE_NOT_FOUND";
		if (isUndeclaredException)
			type = "UNDECLARED";
		sb.append(type);
		sb.append(", exception: ");
		sb.append(this.cause.toString());
		sb.append("}");
		return sb.toString();
	}
}
