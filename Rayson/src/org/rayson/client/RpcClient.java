package org.rayson.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.api.IllegalServiceException;
import org.rayson.api.RpcService;
import org.rayson.api.ServerService;
import org.rayson.api.ServiceRegistration;
import org.rayson.api.ServiceNotFoundException;
import org.rayson.common.Invocation;
import org.rayson.common.RpcException;
import org.rayson.transport.client.TransportClient;

public class RpcClient {

	private static class RpcServiceKey {
		private int hash;
		private SocketAddress serverAddress;
		private Class<? extends RpcService> serviceClass;
		private String serviceName;

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

		private ClientCall lastCall;
		private SocketAddress serverAddress;
		private String serviceName;

		public RpcServiceProxy(String serviceName, SocketAddress serverAddress) {
			this.serviceName = serviceName;
			this.serverAddress = serverAddress;
		}

		private Object callLast() throws Throwable {
			ClientCall call = lastCall;
			if (lastCall == null)
				throw new IllegalStateException("No submitted last call");
			lastCall = null;
			try {
				RpcClient.getInstance().submitCall(serverAddress, call);
			} catch (Throwable e) {
				throw new RpcException(e);
			}
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

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if (lastCall != null) {
				// clean up.
				lastCall = null;
				throw new IllegalStateException("Last call not be called yet");
			}
			getInstance().lastProxy.set(this);
			Invocation invocation = new Invocation(serviceName, method, args);
			this.lastCall = new ClientCall(invocation);
			return emptyReturnValueFor(method.getReturnType());
		}
	}

	private static Map<Class<?>, Object> emptyReturnValues = new HashMap<Class<?>, Object>();

	private static RpcClient instance = new RpcClient();

	static {
		emptyReturnValues.put(Void.TYPE, null);
		emptyReturnValues.put(Boolean.TYPE, Boolean.FALSE);
		emptyReturnValues.put(Byte.TYPE, Byte.valueOf((byte) 0));
		emptyReturnValues.put(Short.TYPE, Short.valueOf((short) 0));
		emptyReturnValues.put(Character.TYPE, Character.valueOf((char) 0));
		emptyReturnValues.put(Integer.TYPE, Integer.valueOf(0));
		emptyReturnValues.put(Long.TYPE, Long.valueOf(0));
		emptyReturnValues.put(Float.TYPE, Float.valueOf(0));
		emptyReturnValues.put(Double.TYPE, Double.valueOf(0));
	}

	public static Object emptyReturnValueFor(final Class<?> type) {
		return type.isPrimitive() ? emptyReturnValues.get(type) : null;
	}

	public static RpcClient getInstance() {
		return instance;
	}

	public static void main(String[] args) throws UnknownHostException,
			RpcException, ServiceNotFoundException, IllegalServiceException {
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);

		ServerService rpcService = RpcClient.getInstance().createProxy(
				ServerService.class, "server", serverAddress);
		ServiceRegistration[] serviceDescriptions = rpcService.list();
		for (ServiceRegistration serviceDescription : serviceDescriptions) {
			System.out.println(serviceDescription.toString());
		}
	}

	public static void throwRpcExceptionCause(RpcException rpcException)
			throws IOException, ServiceNotFoundException,
			UndeclaredThrowableException {
		Throwable cause = rpcException.getCause();
		if (IOException.class.isAssignableFrom(cause.getClass()))
			throw (IOException) cause;
		else if (ServiceNotFoundException.class.isAssignableFrom(cause
				.getClass()))
			throw (ServiceNotFoundException) cause;
		else
			throw new UndeclaredThrowableException(cause);

	}

	private ConcurrentHashMap<Long, ClientCall<?>> calls;
	private ThreadLocal<RpcServiceProxy> lastProxy;
	private AtomicBoolean loaded = new AtomicBoolean(false);

	private ResponseWorker responseWorker;

	private WeakHashMap<RpcServiceKey, RpcService> serviceProxys;

	private RpcClient() {
	}

	public <T extends RpcService> T createProxy(Class<T> serviceClass,
			String serviceName, SocketAddress serverAddress)
			throws IllegalServiceException {
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

	public <T> T call(T value) throws IOException, ServiceNotFoundException,
			UndeclaredThrowableException {
		RpcServiceProxy serviceProxy = lastProxy.get();
		if (serviceProxy == null)
			throw new IllegalStateException(
					"Thread local last proxy is not found");
		try {
			return (T) lastProxy.get().callLast();
		} catch (RpcException rpcException) {
			Throwable cause = rpcException.getCause();
			if (IOException.class.isAssignableFrom(cause.getClass()))
				throw (IOException) cause;
			else if (ServiceNotFoundException.class.isAssignableFrom(cause
					.getClass()))
				throw (ServiceNotFoundException) cause;
			else
				throw new UndeclaredThrowableException(cause);
		} catch (Throwable e) {
			// never be there.
			throw new RuntimeException(e);
		}
	}

	private void lazyLoad() {
		calls = new ConcurrentHashMap<Long, ClientCall<?>>();
		serviceProxys = new WeakHashMap<RpcClient.RpcServiceKey, RpcService>();
		responseWorker = new ResponseWorker();
		responseWorker.start();
		lastProxy = new ThreadLocal<RpcClient.RpcServiceProxy>();
	}

	private void submitCall(SocketAddress serverAddress, ClientCall call)
			throws IOException {
		calls.put(call.getId(), call);
		TransportClient.getInstance().getConnector()
				.sumbitCall(serverAddress, call);
	}
}
