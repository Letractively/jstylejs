package org.rayson.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketAddress;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rayson.api.IllegalServiceException;
import org.rayson.api.RpcService;
import org.rayson.api.ServiceNotFoundException;
import org.rayson.common.Invocation;
import org.rayson.common.RpcException;
import org.rayson.transport.client.TransportClient;
import org.rayson.util.Reflection;

class RpcClient {

	private class CallWrapper {
		private RpcServiceProxy lastProxy;
		private ClientCall lastCall;

		public void setLast(RpcServiceProxy lastProxy, ClientCall lastCall) {
			if (this.lastCall != null) {
				// Log the illegal state information.
				LOGGER.log(Level.SEVERE, "Last call " + lastCall.toString()
						+ " was not submitted to remote server");
				// We still need to prepare to call the last call.
			}
			this.lastCall = lastCall;
			this.lastProxy = lastProxy;
		}

		CallWrapper() {

		}

		public Object callLast() throws IllegalCallStateException, Throwable {
			ClientCall call = lastCall;
			if (lastCall == null) {
				// Log the illegal state information.
				throw new IllegalCallStateException("Last call  not found");
			}
			lastCall = null;
			try {
				RpcClient.this.submitCall(this.lastProxy.serverAddress, call);
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
	}

	private class ThreadLocalCall extends ThreadLocal<CallWrapper> {
		@Override
		protected CallWrapper initialValue() {
			return new CallWrapper();
		}
	}

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

	private class RpcServiceProxy implements InvocationHandler {

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
			threadLocalCall.get().setLast(this, call);
			return Reflection.emptyReturnValueFor(method.getReturnType());
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
	private AtomicBoolean loaded = new AtomicBoolean(false);
	private ThreadLocalCall threadLocalCall;
	private ResponseWorker responseWorker;
	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private WeakHashMap<RpcServiceKey, RpcService> serviceProxys;

	RpcClient() {
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
		try {
			return (T) threadLocalCall.get().callLast();
		} catch (RpcException rpcException) {
			Throwable cause = rpcException.getCause();
			if (IOException.class.isAssignableFrom(cause.getClass()))
				throw (IOException) cause;
			else if (ServiceNotFoundException.class.isAssignableFrom(cause
					.getClass()))
				throw (ServiceNotFoundException) cause;
			else
				throw new UndeclaredThrowableException(cause);
		} catch (IllegalCallStateException e) {
			e.printStackTrace();
			// do nothing.
			return value;
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
		threadLocalCall = new ThreadLocalCall();
	}

	private void submitCall(SocketAddress serverAddress, ClientCall call)
			throws IOException {
		calls.put(call.getId(), call);
		TransportClient.getSingleton().getConnector()
				.sumbitCall(serverAddress, call);
	}
}