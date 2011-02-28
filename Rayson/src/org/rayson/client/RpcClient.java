package org.rayson.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.rayson.api.IllegalServiceException;
import org.rayson.api.RpcService;
import org.rayson.api.ServiceNotFoundException;
import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.impl.RemoteExceptionImpl;
import org.rayson.transport.client.TransportClient;
import org.rayson.util.Log;

class RpcClient {
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
			try {
				submitCall(serverAddress, call);
			} catch (IOException e) {
				throw RemoteExceptionImpl.createNetWorkException(e);
			}
			try {
				return call.getResult();
			} catch (ExecutionException e) {
				InvocationException invocationException = (InvocationException) e
						.getCause();
				Throwable remoteException = invocationException
						.getRemoteException();
				StackTraceElement[] stackTraceElements = Thread.currentThread()
						.getStackTrace();
				remoteException.setStackTrace(Arrays.copyOfRange(
						stackTraceElements, stackTraceElements.length - 1,
						stackTraceElements.length));
				if (ServiceNotFoundException.class
						.isAssignableFrom(remoteException.getClass()))
					throw RemoteExceptionImpl
							.createServiceNotFoundException((ServiceNotFoundException) remoteException);
				if (invocationException.isUnDeclaredException())
					throw RemoteExceptionImpl
							.createUndecleardException(remoteException);
				else
					throw remoteException;
			}
		}
	}

	private ConcurrentHashMap<Long, ClientCall<?>> calls;
	private ResponseWorker responseWorker;
	private static Logger LOGGER = Log.getLogger();
	private WeakHashMap<RpcServiceKey, RpcService> serviceProxys;

	RpcClient() {
	}

	public <T extends RpcService> T createProxy(Class<T> serviceClass,
			String serviceName, SocketAddress serverAddress)
			throws IllegalServiceException {

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

	<T> T call(final T rpcCall) throws IOException, ServiceNotFoundException,
			UndeclaredThrowableException {
		return rpcCall;
	}

	void initialize() {
		calls = new ConcurrentHashMap<Long, ClientCall<?>>();
		serviceProxys = new WeakHashMap<RpcClient.RpcServiceKey, RpcService>();
		responseWorker = new ResponseWorker();
		responseWorker.start();
	}

	private void submitCall(SocketAddress serverAddress, ClientCall call)
			throws IOException {
		calls.put(call.getId(), call);
		TransportClient.getSingleton().getConnector()
				.sumbitCall(serverAddress, call);
	}
}