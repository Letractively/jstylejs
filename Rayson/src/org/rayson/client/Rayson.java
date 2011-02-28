package org.rayson.client;

import java.io.IOException;
import java.net.SocketAddress;

import org.rayson.api.IllegalServiceException;
import org.rayson.api.RpcService;
import org.rayson.api.ServiceNotFoundException;

public final class Rayson {
	private static RpcClient client = new RpcClient();

	public static <T extends RpcService> T createProxy(Class<T> serviceClass,
			String serviceName, SocketAddress serverAddress)
			throws IllegalServiceException {
		return client.createProxy(serviceClass, serviceName, serverAddress);
	}

	public static <T> T call(T rpcCall) throws IOException,
			ServiceNotFoundException, Throwable {
		return client.call(rpcCall);
	}
}
