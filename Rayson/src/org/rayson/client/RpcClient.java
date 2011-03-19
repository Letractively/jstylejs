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

import org.rayson.api.ActivitySocket;
import org.rayson.api.RpcProxy;
import org.rayson.api.ServerProxy;
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
import org.rayson.server.ServerService;
import org.rayson.transport.client.TransportClient;

class RpcClient {

	private class RpcProxyInvoker implements InvocationHandler, RpcProxy {
		private ClientSession currentSession;

		public RpcProxyInvoker(String serviceName, SocketAddress serverAddress) {
			long sessionId = System.currentTimeMillis() + this.hashCode();
			long creationTime = System.currentTimeMillis();
			this.currentSession = new ClientSession(version, sessionId,
					serviceName, creationTime, serverAddress);
		}

		private ClientSession touchAndGetSession() {
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

	private static byte version = 1;

	public static byte getVersion() {
		return version;
	}

	private ResponseWorker responseWorker;

	private WeakHashMap<SocketAddress, ServerProxy> serverServices;

	RpcClient() {
	}

	<T> T call(final T rpcCall) throws IOException, ServiceNotFoundException,
			UndeclaredThrowableException {
		return rpcCall;
	}

	public <T extends RpcProxy> T createRpcProxy(String serviceName,
			Class<T> serviceClass, SocketAddress serverAddress)
			throws IllegalServiceException, RpcException {
		ServerProxy serverService = getServerProxy(serverAddress);
		T rpcService;
		rpcService = (T) Proxy.newProxyInstance(
				RpcClient.class.getClassLoader(), new Class[] { serviceClass },
				new RpcProxyInvoker(serviceName, serverAddress));
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
	<T extends RpcProxy> T getServerProxy(SocketAddress serverAddress)
			throws IllegalServiceException {
		ServerProxy rpcService;
		synchronized (serverServices) {
			rpcService = serverServices.get(serverAddress);
			if (rpcService == null) {
				rpcService = (ServerProxy) Proxy.newProxyInstance(
						RpcClient.class.getClassLoader(),
						new Class[] { ServerProxy.class }, new RpcProxyInvoker(
								ServerService.NAME, serverAddress));
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

	public ActivitySocket openSocket(short activity) throws IOException,
			ServiceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}