package org.rayson.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.SocketAddress;

import org.rayson.rpc.RpcService;

public class RpcClient {

	private static class Invoker implements InvocationHandler {

		private String serviceName;
		private SocketAddress serverAddress;

		public Invoker(String serviceName, SocketAddress serverAddress) {
			this.serviceName = serviceName;
			this.serverAddress = serverAddress;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {

			return null;
		}

	}

	private static RpcClient instance = new RpcClient();

	private RpcClient() {

	}

	public static RpcClient getInstance() {
		return instance;
	}

	public <T extends RpcService> T createProxy(Class<T> service,
			String serviceName, SocketAddress serverAddress) {
		return (T) Proxy.newProxyInstance(service.getClassLoader(),
				new Class[] { service },
				new Invoker(serviceName, serverAddress));
	}
}
