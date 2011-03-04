package org.rayson.client;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.api.RpcService;
import org.rayson.api.ServerService;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.NetWorkException;

public final class Rayson {
	private static RpcClient client = new RpcClient();
	private static AtomicBoolean clientInited = new AtomicBoolean(false);

	public static <T extends RpcService> T getService(String serviceName,
			Class<T> serviceClass, SocketAddress serverAddress)
			throws IllegalServiceException {
		synchronized (clientInited) {
			if (clientInited.compareAndSet(false, true))
				client.initialize();
		}
		return client.getRpcProxy(serviceClass, serviceName, serverAddress);
	}

	public static ServerService getServerService(SocketAddress serverAddress) {
		try {
			return getService(ServerService.NAME, ServerService.class,
					serverAddress);
		} catch (IllegalServiceException e) {
			throw new RuntimeException("Server service is illeagal", e);
		}
	}

	public static void ping(SocketAddress serverAddress)
			throws NetWorkException {
		client.ping(serverAddress);
	}
}
