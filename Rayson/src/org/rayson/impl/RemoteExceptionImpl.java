package org.rayson.impl;

import java.io.IOException;

import org.rayson.exception.NetWorkException;
import org.rayson.exception.RemoteException;
import org.rayson.exception.RemoteExceptionType;
import org.rayson.exception.ServiceNotFoundException;

public final class RemoteExceptionImpl extends RemoteException {

	private static final long serialVersionUID = 1L;

	private RemoteExceptionImpl(RemoteExceptionType type, Throwable exception) {
		super(type, exception);
	}

	public static RemoteException createNetWorkException(IOException cause) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(
				RemoteExceptionType.NETWORK, new NetWorkException(cause));
		return remoteException;
	}

	public static RemoteException createServiceNotFoundException(
			ServiceNotFoundException exception) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(
				RemoteExceptionType.SERVICE_NOT_FOUND, exception);
		return remoteException;
	}

	public static RemoteException createUndecleardException(Throwable throwable) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(
				RemoteExceptionType.UNDECLARED, throwable);
		return remoteException;
	}
}
