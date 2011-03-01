package org.rayson.client;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.api.RpcService;
import org.rayson.exception.IllegalServiceException;

public final class Rayson {
	private static RpcClient client = new RpcClient();
	private static AtomicBoolean clientInited = new AtomicBoolean(false);

	public static <T extends RpcService> T createProxy(String serviceName,
			Class<T> serviceClass, SocketAddress serverAddress)
			throws IllegalServiceException {
		synchronized (clientInited) {
			if (clientInited.compareAndSet(false, true))
				client.initialize();
		}
		return client.createProxy(serviceClass, serviceName, serverAddress);
	}
}
