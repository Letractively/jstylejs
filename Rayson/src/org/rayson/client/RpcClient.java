package org.rayson.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.SocketAddress;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.RpcService;
import org.rayson.io.Invocation;
import org.rayson.transport.client.TransportClient;

public class RpcClient {

	private static class RpcServiceKey {
		private SocketAddress serverAddress;
		private int hash;
		private String serviceName;

		public RpcServiceKey(String serviceName, SocketAddress serverAddress) {
			this.serverAddress = serverAddress;
			this.serviceName = serviceName;
			this.hash = serviceName.hashCode() + serverAddress.hashCode();
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (!(obj instanceof RpcServiceKey))
				return false;
			RpcServiceKey to = (RpcServiceKey) obj;
			return to.serverAddress.equals(this.serverAddress)
					&& to.serviceName.equals(this.serviceName);
		}

	}

	private static class RpcServiceProxy implements InvocationHandler {

		private String serviceName;
		private SocketAddress serverAddress;

		public RpcServiceProxy(String serviceName, SocketAddress serverAddress) {
			this.serviceName = serviceName;
			this.serverAddress = serverAddress;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			Invocation invocation = new Invocation(serviceName, method, args);
			ClientCall call = new ClientCall(invocation);
			RpcClient.getInstance().submitCall(serverAddress, call);
			return call.getResult();
		}

	}

	private static RpcClient instance = new RpcClient();
	private ConcurrentHashMap<Long, ClientCall<?>> calls;
	private WeakHashMap<RpcServiceKey, RpcService> serviceProxys;
	private AtomicBoolean loaded = new AtomicBoolean(false);

	private RpcClient() {
	}

	private void submitCall(SocketAddress serverAddress, ClientCall call)
			throws IOException {
		calls.put(call.getId(), call);
		TransportClient.getInstance().getConnector()
				.sumbitCall(serverAddress, call);
	}

	public static RpcClient getInstance() {
		return instance;
	}

	public <T extends RpcService> T createProxy(Class<T> service,
			String serviceName, SocketAddress serverAddress) {
		synchronized (loaded) {
			if (loaded.compareAndSet(false, true))
				lazyLoad();
		}

		RpcServiceKey serviceKey = new RpcServiceKey(serviceName, serverAddress);
		RpcService rpcService;
		synchronized (serviceProxys) {
			rpcService = serviceProxys.get(serviceKey);
			if (rpcService == null) {
				rpcService = (RpcService) Proxy.newProxyInstance(
						service.getClassLoader(), new Class[] { service },
						new RpcServiceProxy(serviceName, serverAddress));
				serviceProxys.put(serviceKey, rpcService);
			}

		}
		return (T) rpcService;
	}

	private void lazyLoad() {
		calls = new ConcurrentHashMap<Long, ClientCall<?>>();
		serviceProxys = new WeakHashMap<RpcClient.RpcServiceKey, RpcService>();
	}
}
