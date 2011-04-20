/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.impl;

import java.io.IOException;
import org.creativor.rayson.exception.CallException;
import org.creativor.rayson.exception.NetWorkException;
import org.creativor.rayson.exception.RemoteExceptionType;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.exception.UnsupportedVersionException;

/**
 *
 * @author Nick Zhang
 */
public final class RpcExceptionImpl extends RpcException {

	private static final long serialVersionUID = 1L;

	public static RpcException createNetWorkException(IOException cause) {
		RpcExceptionImpl remoteException = new RpcExceptionImpl(
				RemoteExceptionType.NETWORK, new NetWorkException(cause));
		return remoteException;
	}

	public static RpcException createUnsupportedProxyVersion(
			UnsupportedVersionException exception) {
		RpcExceptionImpl remoteException = new RpcExceptionImpl(
				RemoteExceptionType.UNSUPPORTED_PROXY_VERSION, exception);
		return remoteException;

	}

	public static RpcException createParameterException(CallException exception) {
		RpcExceptionImpl remoteException = new RpcExceptionImpl(
				RemoteExceptionType.CALL, exception);
		return remoteException;
	}

	public static RpcException createServiceNotFoundException(
			ServiceNotFoundException exception) {
		RpcExceptionImpl remoteException = new RpcExceptionImpl(
				RemoteExceptionType.SERVICE_NOT_FOUND, exception);
		return remoteException;
	}

	public static RpcException createUndecleardException(Throwable throwable) {
		RpcExceptionImpl remoteException = new RpcExceptionImpl(
				RemoteExceptionType.UNDECLARED, throwable);
		return remoteException;
	}

	private RpcExceptionImpl(RemoteExceptionType type, Throwable exception) {
		super(type, exception);
	}
}
