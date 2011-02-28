package org.rayson.impl;

import java.io.IOException;

import org.rayson.api.NetWorkException;
import org.rayson.api.RemoteException;
import org.rayson.api.ServiceNotFoundException;

public final class RemoteExceptionImpl extends RemoteException {

	private static final long serialVersionUID = 1L;

	public RemoteExceptionImpl(Throwable exception) {
		super(exception);
	}

	public static RemoteException createNetWorkException(IOException cause) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(
				new NetWorkException(cause));
		remoteException.isNetworkException = true;
		return remoteException;
	}

	public static RemoteException createServiceNotFoundException(
			ServiceNotFoundException exception) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(exception);
		remoteException.isServiceNotFound = true;
		return remoteException;
	}

	public static RemoteException createUndecleardException(Throwable throwable) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(throwable);
		remoteException.isUndeclaredException = true;
		return remoteException;
	}
}
