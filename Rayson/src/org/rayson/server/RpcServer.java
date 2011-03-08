package org.rayson.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.rayson.api.RpcService;
import org.rayson.api.ServerProtocol;
import org.rayson.api.ServiceRegistration;
import org.rayson.api.Session;
import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.impl.ServiceDescriptionImpl;
import org.rayson.transport.server.TransportServerImpl;

class RpcServer extends TransportServerImpl implements ServerService {
	private static final String DEFAULT_SERVICE_DESCRIPTION = "Rpc server default service";
	private static final int DEFAULT_WORKER_COUNT = 4;
	private static final String LOG_IN_METHOD_NAME = "logIn";

	public static void main(String[] args) throws IOException {

		RpcServer server = new RpcServer(PORT_NUMBER);
		server.start();
	}

	private HashMap<String, Service> services;

	RpcServer(int portNum) {
		super(portNum);
		services = new HashMap<String, Service>();
	}

	@Override
	public ServiceRegistration find(Session session, String serviceName)
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
		Invocation invocation = call.getInvocation();
		RpcService serviceObject;
		try {
			serviceObject = getService(invocation.getServiceName())
					.getInstance();
			Object result;
			// try log in
			if (call.getSessionId() < 0
					&& LOG_IN_METHOD_NAME.equals(invocation.getMethodName())) {
				result = logIn();
			} else {
				Session session = SessionFactory
						.getSession(call.getSessionId());
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
	public ServiceRegistration[] list(Session session) {
		List<ServiceRegistration> list = new ArrayList<ServiceRegistration>();
		for (Entry<String, Service> entry : services.entrySet()) {
			Service service = entry.getValue();
			list.add(new ServiceDescriptionImpl(service.getName(), service
					.getDescription(), service.getProtocols()));
		}
		return list.toArray(new ServiceDescriptionImpl[0]);
	}

	/**
	 * Log into this server.
	 * 
	 * @return The new session's id.
	 */
	public long logIn() {
		return System.nanoTime();
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
		// TODO: start this RPC server .
		for (int i = 0; i < DEFAULT_WORKER_COUNT; i++) {
			CallWorker callWorker = new CallWorker(this);
			callWorker.start();
		}
	}

}