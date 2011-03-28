package org.rayson.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketAddress;
import java.util.WeakHashMap;

import org.rayson.annotation.TransferCode;
import org.rayson.api.AsyncProxy;
import org.rayson.api.CallFuture;
import org.rayson.api.RpcProxy;
import org.rayson.api.ServerProxy;
import org.rayson.api.Session;
import org.rayson.api.TransferArgument;
import org.rayson.api.TransferSocket;
import org.rayson.common.ClientSession;
import org.rayson.common.Invocation;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.NetWorkException;
import org.rayson.exception.RpcException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.impl.RemoteExceptionImpl;
import org.rayson.server.ServerService;
import org.rayson.transport.client.TransportClient;
import org.rayson.util.ServiceVerifier;

class Client {

	private class RpcProxyInvoker implements InvocationHandler, RpcProxy {
		private ClientSession currentSession;

		public RpcProxyInvoker(String serviceName, SocketAddress serverAddress) {
			long sessionId = System.currentTimeMillis() + this.hashCode();
			long creationTime = System.currentTimeMillis();
			this.currentSession = new ClientSession(version, sessionId,
					serviceName, creationTime, serverAddress);
		}

		protected ClientSession touchAndGetSession() {
			this.currentSession.touch();
			return this.currentSession;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// If it's getSession method, then invoke locally.
			if (method.getName().equals("getSession") && args == null)
				return getSession();
			Invocation invocation = new Invocation(method, args);
			return invokeRpcCall(touchAndGetSession(), proxy, invocation);
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("session: ");
			sb.append(currentSession.toString());
			sb.append("}");
			return sb.toString();
		}

		@Override
		public Session getSession() {
			return currentSession;
		}
	}

	private class AsyncProxyInvoker extends RpcProxyInvoker {

		public AsyncProxyInvoker(String serviceName, SocketAddress serverAddress) {
			super(serviceName, serverAddress);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// If it's getSession method, then invoke locally.
			if (method.getName().equals("getSession") && args == null)
				return getSession();
			Invocation invocation = new Invocation(method, args);
			return invokeAsyncCall(touchAndGetSession(), proxy, invocation);
		}
	}

	private static byte version = 1;

	public static byte getVersion() {
		return version;
	}

	private ResponseWorker responseWorker;

	private WeakHashMap<SocketAddress, ServerProxy> serverServices;

	Client() {
	}

	<T> T call(final T rpcCall) throws IOException, ServiceNotFoundException,
			UndeclaredThrowableException {
		return rpcCall;
	}

	public <T extends RpcProxy> T createRpcProxy(String serviceName,
			Class<T> proxyInterface, SocketAddress serverAddress)
			throws IllegalServiceException, RpcException {
		if (serviceName == null)
			throw new IllegalArgumentException(
					"Service name should not be null");
		if (proxyInterface == null)
			throw new IllegalArgumentException(
					"Proxy interface  should not be null");
		if (serverAddress == null)
			throw new IllegalArgumentException(
					"Server address name should not be null");
		// verify rpc proxy interface.
		if (!proxyInterface.isInterface())
			throw new IllegalServiceException("Proxy interface "
					+ proxyInterface.getName() + " must be an interface");
		for (Method proxyMethod : proxyInterface.getMethods()) {
			// Ignore getSession method.
			if (proxyMethod.getName().equals("getSession")
					&& proxyMethod.getParameterTypes().length == 0)
				continue;
			ServiceVerifier.verifyProxyMethod(proxyMethod);
		}

		T rpcProxy;
		rpcProxy = (T) Proxy.newProxyInstance(Client.class.getClassLoader(),
				new Class[] { proxyInterface }, new RpcProxyInvoker(
						serviceName, serverAddress));
		return rpcProxy;
	}

	public <T extends AsyncProxy> T createAsyncProxy(String serviceName,
			Class<T> proxyInterface, SocketAddress serverAddress)
			throws IllegalServiceException, RpcException {
		if (serviceName == null)
			throw new IllegalArgumentException(
					"Service name should not be null");
		if (proxyInterface == null)
			throw new IllegalArgumentException(
					"Proxy interface  should not be null");
		if (serverAddress == null)
			throw new IllegalArgumentException(
					"Server address name should not be null");

		// verify rpc proxy interface.
		if (!proxyInterface.isInterface())
			throw new IllegalServiceException("Proxy interface "
					+ proxyInterface.getName() + " must be an interface");
		for (Method proxyMethod : proxyInterface.getMethods()) {
			// Ignore getSession method.
			if (proxyMethod.getName().equals("getSession")
					&& proxyMethod.getParameterTypes().length == 0)
				continue;
			ServiceVerifier.verifyAsyncProxyMethod(proxyMethod);
		}

		T rpcProxy;
		rpcProxy = (T) Proxy.newProxyInstance(Client.class.getClassLoader(),
				new Class[] { proxyInterface }, new AsyncProxyInvoker(
						serviceName, serverAddress));
		return rpcProxy;
	}

	/**
	 * @param <T>
	 * @param serviceClass
	 * @param serviceName
	 * @param serverAddress
	 * @return
	 * @throws IllegalServiceException
	 */
	<T extends RpcProxy> T getServerProxy(SocketAddress serverAddress)
			throws IllegalServiceException {
		ServerProxy rpcService;
		synchronized (serverServices) {
			rpcService = serverServices.get(serverAddress);
			if (rpcService == null) {
				rpcService = (ServerProxy) Proxy.newProxyInstance(Client.class
						.getClassLoader(), new Class[] { ServerProxy.class },
						new RpcProxyInvoker(ServerService.NAME, serverAddress));
				serverServices.put(serverAddress, rpcService);
			}

		}
		return (T) rpcService;
	}

	void initialize() {
		serverServices = new WeakHashMap<SocketAddress, ServerProxy>();
		responseWorker = new ResponseWorker();
		responseWorker.start();
	}

	private Object invokeRpcCall(ClientSession clientSession, Object proxy,
			Invocation invocation) throws Throwable {
		ClientCall call = new ClientCall(clientSession, invocation);
		try {
			submitCall(clientSession.getPeerAddress(), call);
		} catch (IOException e) {
			throw RemoteExceptionImpl.createNetWorkException(e);
		}
		try {
			return call.getResult();
		} catch (Throwable e) {
			throw RemoteExceptionImpl.createUndecleardException(e);
		}
	}

	private CallFuture invokeAsyncCall(ClientSession clientSession,
			Object proxy, Invocation invocation) throws Throwable {
		ClientCall call = new ClientCall(clientSession, invocation);
		try {
			submitCall(clientSession.getPeerAddress(), call);
		} catch (IOException e) {
			throw new NetWorkException(e);
		}
		return call.getFuture();
	}

	public void ping(SocketAddress serverAddress) throws NetWorkException {
		TransportClient.getSingleton().getConnector().ping(serverAddress);
	}

	private void submitCall(SocketAddress serverAddress, ClientCall call)
			throws IOException {
		TransportClient.getSingleton().getConnector()
				.sumbitCall(serverAddress, call);
	}

	public TransferSocket openTransferSocket(SocketAddress serverAddress,
			TransferArgument argument) throws IOException,
			ServiceNotFoundException, IllegalServiceException {
		if (argument == null)
			throw new IllegalArgumentException(
					"Transfer argument  should not be null");
		if (serverAddress == null)
			throw new IllegalArgumentException(
					"Server address name should not be null");
		// Verify transfer code.
		if (argument.getClass().getAnnotation(TransferCode.class) == null)
			throw new IllegalServiceException(
					"No transfer code annotation found in argument class");
		return TransportClient.getSingleton().getConnector()
				.openTransferSocket(serverAddress, argument);
	}

}