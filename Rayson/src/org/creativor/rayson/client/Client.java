package org.creativor.rayson.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.WeakHashMap;

import org.creativor.rayson.annotation.ClientVersion;
import org.creativor.rayson.api.AsyncProxy;
import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.api.ServerProxy;
import org.creativor.rayson.api.TransferArgument;
import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.client.impl.CallFutureImpl;
import org.creativor.rayson.common.ClientSession;
import org.creativor.rayson.common.Invocation;
import org.creativor.rayson.common.InvocationException;
import org.creativor.rayson.exception.CallExecutionException;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.NetWorkException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.impl.RpcExceptionImpl;
import org.creativor.rayson.server.ServerService;
import org.creativor.rayson.transport.client.TransportClient;
import org.creativor.rayson.util.ServiceVerifier;

class Client {
	private class AsyncProxyInvoker extends RpcProxyInvoker {

		public AsyncProxyInvoker(short version, String serviceName,
				InetSocketAddress serverAddress) {
			super(version, serviceName, serverAddress);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// If it's getSession method, then invoke locally.
			if (method.getName().equals("getSession") && args == null)
				return getSession();

			getSession().touch();
			if (getSession().isUnsupportedVersion()) {
				CallFutureImpl callFuture = new CallFutureImpl(
						method.getExceptionTypes());
				callFuture.setException(new InvocationException(false,
						getSession().getUnsupportedVersionException()));
				return callFuture;
			}
			Invocation invocation = new Invocation(method, args);

			getSession().touch();

			ClientCall call = new ClientCall(getSession(), invocation,
					new CallFutureImpl(method.getExceptionTypes()));

			try {
				submitCall(getSession().getPeerAddress(), call);
			} catch (IOException e) {
				throw new NetWorkException(e);
			}

			return call.getFuture();
		}
	}

	private class RpcProxyInvoker implements InvocationHandler, RpcProxy {
		private ProxySession session;

		public RpcProxyInvoker(short version, String serviceName,
				InetSocketAddress serverAddress) {
			long sessionId = ClientSession.getNextUID();
			long creationTime = System.currentTimeMillis();
			this.session = new ProxySession(VERSION, version, sessionId,
					serviceName, creationTime, serverAddress);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// If it's getSession method, then invoke locally.
			if (method.getName().equals("getSession") && args == null)
				return getSession();
			session.touch();
			if (getSession().isUnsupportedVersion()) {
				CallFutureImpl callFuture = new CallFutureImpl(
						method.getExceptionTypes());
				callFuture.setException(new InvocationException(false,
						getSession().getUnsupportedVersionException()));
				return callFuture.get();
			}

			Invocation invocation = new Invocation(method, args);

			ClientCall call = new ClientCall(session, invocation,
					new CallFutureImpl(method.getExceptionTypes()));

			try {
				submitCall(session.getPeerAddress(), call);
			} catch (IOException e) {
				throw RpcExceptionImpl.createNetWorkException(e);
			}

			try {
				return call.getResult();
			} catch (CallExecutionException e) {
				throw e.getCause();
			} catch (InterruptedException e) {
				throw RpcExceptionImpl.createUndecleardException(e);
			}
		}

		@Override
		public ProxySession getSession() {
			return session;
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("session: ");
			sb.append(session.toString());
			sb.append("}");
			return sb.toString();
		}
	}

	private static short SERVER_PROXY_VERSION = 0;

	private static byte VERSION = 1;

	public static byte getVersion() {
		return VERSION;
	}

	private ResponseWorker responseWorker;

	private WeakHashMap<SocketAddress, ServerProxy> serverServices;

	Client() {
	}

	<T> T call(final T rpcCall) throws IOException, ServiceNotFoundException,
			UndeclaredThrowableException {
		return rpcCall;
	}

	public <T extends AsyncProxy> T createAsyncProxy(String serviceName,
			Class<T> proxyInterface, InetSocketAddress serverAddress)
			throws IllegalServiceException {
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
						getProxyVersion(proxyInterface), serviceName,
						serverAddress));
		return rpcProxy;
	}

	public <T extends RpcProxy> T createRpcProxy(String serviceName,
			Class<T> proxyInterface, InetSocketAddress serverAddress)
			throws IllegalServiceException {
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
						getProxyVersion(proxyInterface), serviceName,
						serverAddress));
		return rpcProxy;
	}

	private short getProxyVersion(Class<? extends RpcProxy> proxyInterface) {
		short proxyVersion = ClientVersion.DEFAULT_VALUE;
		ClientVersion annotation = proxyInterface
				.getAnnotation(ClientVersion.class);
		if (annotation != null)
			proxyVersion = annotation.value();
		return proxyVersion;
	}

	/**
	 * @param <T>
	 * @param serviceClass
	 * @param serviceName
	 * @param serverAddress
	 * @return
	 * @throws IllegalServiceException
	 */
	<T extends RpcProxy> T getServerProxy(InetSocketAddress serverAddress)
			throws IllegalServiceException {
		ServerProxy rpcService;
		synchronized (serverServices) {
			rpcService = serverServices.get(serverAddress);
			if (rpcService == null) {
				rpcService = (ServerProxy) Proxy.newProxyInstance(Client.class
						.getClassLoader(), new Class[] { ServerProxy.class },
						new RpcProxyInvoker(SERVER_PROXY_VERSION,
								ServerService.NAME, serverAddress));
				serverServices.put(serverAddress, rpcService);
			}

		}
		return (T) rpcService;
	}

	void initialize() {
		ClientVersion proxyVersion = ServerProxy.class
				.getAnnotation(ClientVersion.class);
		if (proxyVersion != null)
			SERVER_PROXY_VERSION = proxyVersion.value();
		serverServices = new WeakHashMap<SocketAddress, ServerProxy>();
		responseWorker = new ResponseWorker();
		responseWorker.start();
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
		return TransportClient.getSingleton().getConnector()
				.openTransferSocket(serverAddress, argument);
	}

	public void ping(SocketAddress serverAddress) throws NetWorkException {
		TransportClient.getSingleton().getConnector().ping(serverAddress);
	}

	private void submitCall(SocketAddress serverAddress, ClientCall call)
			throws IOException {
		TransportClient.getSingleton().getConnector()
				.submitCall(serverAddress, call);
	}

}