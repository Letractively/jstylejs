package org.rayson.client;

import java.net.SocketAddress;

import org.rayson.api.IllegalServiceException;
import org.rayson.api.RpcService;

public final class Rayson {
	private static RpcClient client = new RpcClient();

	public static <T extends RpcService> T createProxy(Class<T> serviceClass,
			String serviceName, SocketAddress serverAddress)
			throws IllegalServiceException {
		return client.createProxy(serviceClass, serviceName, serverAddress);
	}
}
