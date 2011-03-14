package org.rayson.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.rayson.api.RpcProtocol;
import org.rayson.api.ServerProtocol;
import org.rayson.api.Session;
import org.rayson.common.ClientSession;
import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.exception.CallException;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.NetWorkException;
import org.rayson.exception.RpcException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.impl.RemoteExceptionImpl;
import org.rayson.transport.client.TransportClient;
import org.rayson.util.Log;

class RpcClient {

	private class RpcServiceProxy implements InvocationHandler, Session {

		private SocketAddress serverAddress;
		private String serviceName;
		private long sessionId;
		private long creationTime;
		private long lastAccessedTime;

		public RpcServiceProxy(String serviceName, SocketAddress serverAddress) {
			this.sessionId = System.currentTimeMillis() + this.hashCode();
			this.serviceName = serviceName;
			this.serverAddress = serverAddress;
			this.creationTime = System.currentTimeMillis();
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			Invocation invocation = new Invocation(serviceName, method, args);
			this.lastAccessedTime = System.currentTimeMillis();
			return invokeRpcCall(new ClientSession(protocol, sessionId,
					creationTime, this.lastAccessedTime), serverAddress, proxy,
					invocation);
		}

		@Override
		public long getId() {
			return this.sessionId;
		}

		@Override
		public long getCreationTime() {
			return this.creationTime;
		}

		@Override
		public long getLastAccessedTime() {
			return this.lastAccessedTime;
		}

		@Override
		public byte getProtocol() {
			return protocol;
		}
	}

	private static Logger LOGGER = Log.getLogger();

	private static byte protocol = 1;

	public static byte getProtocol() {
		return protocol;
	}

	private ResponseWorker responseWorker;

	private WeakHashMap<SocketAddress, ServerProtocol> serverServices;

	RpcClient() {
	}

	<T> T call(final T rpcCall) throws IOException, ServiceNotFoundException,
			UndeclaredThrowableException {
		return rpcCall;
	}

	public <T extends RpcProtocol> T createServiceProxy(String serviceName,
			Class<T> serviceClass, SocketAddress serverAddress)
			throws IllegalServiceException, RpcException {
		ServerProtocol serverService = getServerService(serverAddress);
		T rpcService;
		rpcService = (T) Proxy.newProxyInstance(
				RpcClient.class.getClassLoader(), new Class[] { serviceClass },
				new RpcServiceProxy(serviceName, serverAddress));
		return rpcService;
	}

	/**
	 * @param <T>
	 * @param serviceClass
	 * @param serviceName
	 * @param serverAddress
	 * @return
	 * @throws IllegalServiceException
	 */
	<T extends RpcProtocol> T getServerService(SocketAddress serverAddress)
			throws IllegalServiceException {
		ServerProtocol rpcService;
		synchronized (serverServices) {
			rpcService = serverServices.get(serverAddress);
			if (rpcService == null) {
				rpcService = (ServerProtocol) Proxy
						.newProxyInstance(RpcClient.class.getClassLoader(),
								new Class[] { ServerProtocol.class },
								new RpcServiceProxy(ServerProtocol.NAME,
										serverAddress));
				serverServices.put(serverAddress, rpcService);
			}

		}
		return (T) rpcService;
	}

	void initialize() {
		serverServices = new WeakHashMap<SocketAddress, ServerProtocol>();
		responseWorker = new ResponseWorker();
		responseWorker.start();
	}

	private Object invokeRpcCall(ClientSession clientSession,
			SocketAddress serverAddress, Object proxy, Invocation invocation)
			throws Throwable {

		ClientCall call = new ClientCall(clientSession, invocation);
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

			if (invocationException.isUnDeclaredException())
				throw RemoteExceptionImpl
						.createUndecleardException(remoteException);

			if (remoteException instanceof CallException)
				throw RemoteExceptionImpl
						.createParameterException((CallException) remoteException);

			if (remoteException instanceof ConnectionClosedException)

				throw RemoteExceptionImpl
						.createNetWorkException((ConnectionClosedException) remoteException);

			if (remoteException instanceof ServiceNotFoundException)

				throw RemoteExceptionImpl
						.createServiceNotFoundException((ServiceNotFoundException) remoteException);

			throw remoteException;
		}
	}

	public void ping(SocketAddress serverAddress) throws NetWorkException {
		TransportClient.getSingleton().getConnector().ping(serverAddress);
	}

	private void submitCall(SocketAddress serverAddress, ClientCall call)
			throws IOException {
		TransportClient.getSingleton().getConnector()
				.sumbitCall(serverAddress, call);
	}
}