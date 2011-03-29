package org.creativor.rayson.impl;

import java.io.IOException;

import org.creativor.rayson.exception.CallException;
import org.creativor.rayson.exception.NetWorkException;
import org.creativor.rayson.exception.RemoteExceptionType;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.exception.ServiceNotFoundException;

public final class RemoteExceptionImpl extends RpcException {

	private static final long serialVersionUID = 1L;

	public static RpcException createNetWorkException(IOException cause) {
		RemoteExceptionImpl remoteException = new RemoteExceptionImpl(
				RemoteExceptionType.NETWORK, new NetWorkException(cause));
		return remoteException;
	}

	public static RpcException createParameterException(CallException exception) {
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
