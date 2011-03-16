package org.rayson.impl;

import java.io.IOException;

import org.rayson.exception.CallException;
import org.rayson.exception.NetWorkException;
import org.rayson.exception.RemoteExceptionType;
import org.rayson.exception.RpcException;
import org.rayson.exception.ServiceNotFoundException;

public final class RemoteExceptionImpl extends RpcException {

	private static final long serialVersionUID = 1L;

	public static RpcException createNetWorkException(IOException cause) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(
				RemoteExceptionType.NETWORK, new NetWorkException(cause));
		return remoteException;
	}

	public static Exception createParameterException(CallException exception) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(
				RemoteExceptionType.CALL, exception);
		return remoteException;
	}

	public static RpcException createServiceNotFoundException(
			ServiceNotFoundException exception) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(
				RemoteExceptionType.SERVICE_NOT_FOUND, exception);
		return remoteException;
	}

	public static RpcException createUndecleardException(Throwable throwable) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(
				RemoteExceptionType.UNDECLARED, throwable);
		return remoteException;
	}

	private RemoteExceptionImpl(RemoteExceptionType type, Throwable exception) {
		super(type, exception);
	}
}
