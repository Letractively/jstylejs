package org.rayson.client;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.api.RpcService;
import org.rayson.api.ServerService;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.NetWorkException;
import org.rayson.exception.RemoteException;

public final class Rayson {
	private static RpcClient CLIENT = new RpcClient();
	private static AtomicBoolean clientInited = new AtomicBoolean(false);

	private static void tryInit() {
		synchronized (clientInited) {
			if (clientInited.compareAndSet(false, true))
				CLIENT.initialize();
		}
	}

	public static <T extends RpcService> T createServiceProxy(
			String serviceName, Class<T> serviceClass,
			SocketAddress serverAddress) throws IllegalServiceException,
			RemoteException {
		tryInit();
		return CLIENT.createServiceProxy(serviceName, serviceClass,
				serverAddress);
	}

	public static ServerService getServerService(SocketAddress serverAddress) {
		tryInit();

		try {
			return CLIENT.getServerService(serverAddress);
		} catch (IllegalServiceException e) {
			throw new RuntimeException("Server service is illeagal", e);
		}
	}

	public static void ping(SocketAddress serverAddress)
			throws NetWorkException {
		CLIENT.ping(serverAddress);
	}
}
