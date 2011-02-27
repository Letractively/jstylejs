package org.rayson.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.api.RpcException;
import org.rayson.api.RpcService;
import org.rayson.api.ServerService;
import org.rayson.api.ServiceRegistration;
import org.rayson.api.ServiceNotFoundException;
import org.rayson.common.Invocation;
import org.rayson.transport.client.TransportClient;

public class RpcClient {

	private static class RpcServiceKey {
		private int hash;
		private SocketAddress serverAddress;
		private String serviceName;
		private Class<? extends RpcService> serviceClass;

		public RpcServiceKey(String serviceName,
				Class<? extends RpcService> serviceClass,
				SocketAddress serverAddress) {
			this.serverAddress = serverAddress;
			this.serviceName = serviceName;
			this.serviceClass = serviceClass;
			this.hash = serviceClass.getName().hashCode()
					+ serviceName.hashCode() + serverAddress.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (!(obj instanceof RpcServiceKey))
				return false;
			RpcServiceKey to = (RpcServiceKey) obj;
			return to.serverAddress.equals(this.serverAddress)
					&& to.serviceName.equals(this.serviceName)
					&& to.serviceClass.getName().equals(
							this.serviceClass.getName());
		}

		@Override
		public int hashCode() {
			return hash;
		}

	}

	private static class RpcServiceProxy implements InvocationHandler {

		private SocketAddress serverAddress;
		private String serviceName;

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
			Object result;
			try {

				result = call.getResult();

			} catch (ExecutionException e) {
				throw e.getCause();
			} catch (Exception e) {
				throw e;
			}
			return result;
		}

	}

	private static RpcClient instance = new RpcClient();

	public static RpcClient getInstance() {
		return instance;
	}

	private ResponseWorker responseWorker;
	private ConcurrentHashMap<Long, ClientCall<?>> calls;
	private AtomicBoolean loaded = new AtomicBoolean(false);
	private WeakHashMap<RpcServiceKey, RpcService> serviceProxys;

	private RpcClient() {
	}

	public <T extends RpcService> T createProxy(Class<T> serviceClass,
			String serviceName, SocketAddress serverAddress) {
		synchronized (loaded) {
			if (loaded.compareAndSet(false, true))
				lazyLoad();
		}

		RpcServiceKey serviceKey = new RpcServiceKey(serviceName, serviceClass,
				serverAddress);
		RpcService rpcService;
		synchronized (serviceProxys) {
			rpcService = serviceProxys.get(serviceKey);
			if (rpcService == null) {
				rpcService = (RpcService) Proxy.newProxyInstance(serviceClass
						.getClassLoader(), new Class[] { serviceClass },
						new RpcServiceProxy(serviceName, serverAddress));
				serviceProxys.put(serviceKey, rpcService);
			}

		}
		return (T) rpcService;
	}

	private void lazyLoad() {
		calls = new ConcurrentHashMap<Long, ClientCall<?>>();
		serviceProxys = new WeakHashMap<RpcClient.RpcServiceKey, RpcService>();
		responseWorker = new ResponseWorker();
		responseWorker.start();
	}

	private void submitCall(SocketAddress serverAddress, ClientCall call)
			throws IOException {
		calls.put(call.getId(), call);
		TransportClient.getInstance().getConnector()
				.sumbitCall(serverAddress, call);
	}

	public static void main(String[] args) throws UnknownHostException,
			RpcException, ServiceNotFoundException {
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);

		ServerService rpcService = RpcClient.getInstance().createProxy(
				ServerService.class, "server", serverAddress);
		ServiceRegistration[] serviceDescriptions = rpcService.getServices();
		for (ServiceRegistration serviceDescription : serviceDescriptions) {
			System.out.println(serviceDescription.toString());
		}
	}
}
