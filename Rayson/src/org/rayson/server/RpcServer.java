package org.rayson.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.rayson.api.RpcService;
import org.rayson.api.ServerProtocol;
import org.rayson.api.ServiceRegistration;
import org.rayson.common.ClientInfo;
import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.RpcException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.impl.ServiceDescriptionImpl;
import org.rayson.server.api.RpcSession;
import org.rayson.transport.server.TransportServerImpl;

class RpcServer extends TransportServerImpl implements ServerService,
		SessionService {
	private static final String DEFAULT_SERVICE_DESCRIPTION = "Rpc server default service";
	private static final int DEFAULT_WORKER_COUNT = 4;
	private static final String LOG_IN_METHOD_NAME = "logIn";
	private static SessionFactory THE_SESSION_FACTORY = new DefaultSessionFactory();

	private HashMap<String, Service> services;

	RpcServer(int portNum) {
		super(portNum);
		services = new HashMap<String, Service>();
	}

	@Override
	public ServiceRegistration find(RpcSession session, String serviceName)
			throws ServiceNotFoundException {
		Service service = getService(serviceName);
		ServiceDescriptionImpl serviceDescription = new ServiceDescriptionImpl(
				service.getName(), service.getDescription(),
				service.getProtocols());
		return serviceDescription;

	}

	private Service getService(String serviceName)
			throws ServiceNotFoundException {
		Service service = services.get(serviceName);
		if (service == null)
			throw new ServiceNotFoundException(serviceName + " not found");
		return service;
	}

	public void invokeCall(ServerCall call) {
		if (call.exceptionCatched()) {
			// no need to invoke.
			return;
		}

		Object result;
		Invocation invocation = call.getInvocation();
		RpcService serviceObject;

		String serviceName = invocation.getServiceName();
		try {
			if (ServerProtocol.NAME.equals(serviceName)) {
				if (call.getSessionId() < 0
						&& LOG_IN_METHOD_NAME
								.equals(invocation.getMethodName())) {
					// Get client info from client.
					ClientInfo clientInfo;
					try {
						clientInfo = (ClientInfo) invocation.cloneParameter(0);
					} catch (IOException e) {
						throw new InvocationException(true, e);
					}
					// do log in.
					result = logIn(clientInfo, call.getRemoteAddress());
				} else {
					// Server service with no session context.
					result = invocation.invoke(null, this);
				}
			} else {

				serviceObject = getService(serviceName).getInstance();
				// try log in

				SessionImpl session = (SessionImpl) getSessionFactory().get(
						call.getSessionId());
				// touch session.
				session.touch();
				result = invocation.invoke(session, serviceObject);
			}

			call.setResult(result);
		} catch (InvocationException e) {
			call.setException(e);
		} catch (ServiceNotFoundException e) {
			call.setException(new InvocationException(false, e));
		}
	}

	@Override
	public ServiceRegistration[] list(RpcSession session) {
		List<ServiceRegistration> list = new ArrayList<ServiceRegistration>();
		for (Entry<String, Service> entry : services.entrySet()) {
			Service service = entry.getValue();
			list.add(new ServiceDescriptionImpl(service.getName(), service
					.getDescription(), service.getProtocols()));
		}
		return list.toArray(new ServiceDescriptionImpl[0]);
	}

	@Override
	public long logIn(ClientInfo clientInfo, SocketAddress remoteAddr) {
		return this.getSessionFactory().create(clientInfo, remoteAddr).getId();
	}

	public void registerService(String serviceName, String description,
			RpcService serviceInstance) throws ServiceAlreadyExistedException,
			IllegalServiceException {
		synchronized (services) {
			if (services.containsKey(serviceName))
				throw new ServiceAlreadyExistedException(serviceName);
			Service service = new Service(serviceName, description,
					serviceInstance);
			services.put(serviceName, service);
		}
	}

	@Override
	public void start() throws IOException {
		super.start();
		// Register it self as a service.
		try {
			this.registerService(ServerProtocol.NAME,
					DEFAULT_SERVICE_DESCRIPTION, this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (int i = 0; i < DEFAULT_WORKER_COUNT; i++) {
			CallWorker callWorker = new CallWorker(this);
			callWorker.start();
		}
	}

	public SessionFactory getSessionFactory() {
		return THE_SESSION_FACTORY;
	}

	@Override
	public String getServerInfo(RpcSession session) throws RpcException {
		return "Rpc server";
	}

	@Override
	public void logOut(RpcSession session) {
		session.invalidate();
	}
}