/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import org.creativor.rayson.api.AsyncRpcProxy;
import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.api.ServerProxy;
import org.creativor.rayson.api.TransferArgument;
import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.NetWorkException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.exception.UnsupportedVersionException;

/**
 *
 * @author Nick Zhang
 */
public final class Rayson {
	private static Client CLIENT = new Client();
	private static AtomicBoolean clientInited = new AtomicBoolean(false);

	public static <T extends RpcProxy> T createProxy(String serviceName,
			Class<T> proxyInterface, InetSocketAddress serverAddress)
			throws IllegalServiceException {
		tryInit();
		return CLIENT
				.createRpcProxy(serviceName, proxyInterface, serverAddress);
	}

	public static <T extends AsyncRpcProxy> T createAsyncProxy(String serviceName,
			Class<T> proxyInterface, InetSocketAddress serverAddress)
			throws IllegalServiceException {
		tryInit();
		return CLIENT.createAsyncProxy(serviceName, proxyInterface,
				serverAddress);
	}

	public static TransferSocket openTransferSocket(
			SocketAddress serverAddress, TransferArgument argument)
			throws IOException, ServiceNotFoundException,
			IllegalServiceException, UnsupportedVersionException {
		return CLIENT.openTransferSocket(serverAddress, argument);
	}

	public static ServerProxy getServerProxy(InetSocketAddress serverAddress) {
		tryInit();

		try {
			return CLIENT.getServerProxy(serverAddress);
		} catch (IllegalServiceException e) {
			throw new RuntimeException("Server service is illeagal", e);
		}
	}

	public static void ping(SocketAddress serverAddress)
			throws NetWorkException {
		CLIENT.ping(serverAddress);
	}

	public static byte getClientVersion() {
		return Client.getVersion();
	}

	private static void tryInit() {
		synchronized (clientInited) {
			if (clientInited.compareAndSet(false, true))
				CLIENT.initialize();
		}
	}
}
