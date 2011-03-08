package org.rayson.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.rayson.api.RpcProtocol;
import org.rayson.api.ServerProtocol;
import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.exception.CallException;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.NetWorkException;
import org.rayson.exception.RemoteException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.impl.RemoteExceptionImpl;
import org.rayson.transport.client.TransportClient;
import org.rayson.util.Log;

class RpcClient {

	private class RpcServiceProxy implements InvocationHandler {

		private SocketAddress serverAddress;
		private String serviceName;
		private long sessionId;

		public RpcServiceProxy(long sessionId, String serviceName,
				SocketAddress serverAddress) {
			this.sessionId = sessionId;
			this.serviceName = serviceName;
			this.serverAddress = serverAddress;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			Invocation invocation = new Invocation(serviceName, method, args);
			return invokeRpcCall(sessionId, serverAddress, proxy, invocation);
		}
	}

	private static final long EMPTY_SESSSION_ID = -1;

	private static Logger LOGGER = Log.getLogger();

	private static final Invocation SERVER_LOG_IN_INVOCATION;
	static {
		try {
			SERVER_LOG_IN_INVOCATION = new Invocation(ServerProtocol.NAME,
					RpcClient.class.getDeclaredMethod("logIn", null), null);
		} catch (Exception e) {
			throw new RuntimeException("Can ot init SERVER_LOG_IN_INVOCATION",
					e);
		}
	}
	private static long logIn() {
		return -1;
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
			throws IllegalServiceException, RemoteException {
		ServerProtocol serverService = getServerService(serverAddress);
		long sessionId = EMPTY_SESSSION_ID;
		try {
			sessionId = (Long) invokeRpcCall(EMPTY_SESSSION_ID, serverAddress,
					serverService, SERVER_LOG_IN_INVOCATION);
		} catch (RemoteException e) {
			throw e;
		} catch (Throwable e) {
			throw new UndeclaredThrowableException(e);
		}
		T rpcService;
		rpcService = (T) Proxy.newProxyInstance(
				RpcClient.class.getClassLoader(), new Class[] { serviceClass },
				new RpcServiceProxy(sessionId, serviceName, serverAddress));
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
				rpcService = (ServerProtocol) Proxy.newProxyInstance(
						RpcClient.class.getClassLoader(),
						new Class[] { ServerProtocol.class },
						new RpcServiceProxy(EMPTY_SESSSION_ID,
								ServerProtocol.NAME, serverAddress));
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

	private Object invokeRpcCall(long sessionId, SocketAddress serverAddress,
			Object proxy, Invocation invocation) throws Throwable {

		ClientCall call = new ClientCall(sessionId, invocation);
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