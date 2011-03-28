package org.rayson.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.api.AsyncProxy;
import org.rayson.api.TransferArgument;
import org.rayson.api.TransferSocket;
import org.rayson.api.RpcProxy;
import org.rayson.api.ServerProxy;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.NetWorkException;
import org.rayson.exception.RpcException;
import org.rayson.exception.ServiceNotFoundException;

public final class Rayson {
	private static Client CLIENT = new Client();
	private static AtomicBoolean clientInited = new AtomicBoolean(false);

	public static <T extends RpcProxy> T createProxy(String serviceName,
			Class<T> proxyInterface, SocketAddress serverAddress)
			throws IllegalServiceException, RpcException {
		tryInit();
		return CLIENT
				.createRpcProxy(serviceName, proxyInterface, serverAddress);
	}

	public static <T extends AsyncProxy> T createAsyncProxy(String serviceName,
			Class<T> proxyInterface, SocketAddress serverAddress)
			throws IllegalServiceException, RpcException {
		tryInit();
		return CLIENT.createAsyncProxy(serviceName, proxyInterface,
				serverAddress);
	}

	public static TransferSocket openTransferSocket(
			SocketAddress serverAddress, TransferArgument argument)
			throws IOException, ServiceNotFoundException,
			IllegalServiceException {
		return CLIENT.openTransferSocket(serverAddress, argument);
	}

	public static ServerProxy getServerProxy(SocketAddress serverAddress) {
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

	public static byte getCLientVersion() {
		return Client.getVersion();
	}

	private static void tryInit() {
		synchronized (clientInited) {
			if (clientInited.compareAndSet(false, true))
				CLIENT.initialize();
		}
	}
}
