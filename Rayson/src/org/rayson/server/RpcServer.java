package org.rayson.server;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.rayson.api.RpcException;
import org.rayson.api.RpcService;
import org.rayson.api.ServerService;
import org.rayson.api.ServiceDescription;
import org.rayson.api.ServiceNotFoundException;
import org.rayson.common.Invocation;
import org.rayson.impl.ServiceDescriptionImpl;
import org.rayson.transport.server.TransportServerImpl;

class RpcServer extends TransportServerImpl implements ServerService {
	private static final String DEFAULT_SERVICE_NAME = "server";
	private static final int DEFAULT_WORKER_COUNT = 4;
	private HashMap<String, Service> services;

	RpcServer(int portNum) {
		super(portNum);
		services = new HashMap<String, Service>();
	}

	@Override
	public void start() throws IOException {
		super.start();
		// Register it self as a service.
		try {
			this.registerService(DEFAULT_SERVICE_NAME, this);
		} catch (ServiceAlreadyExistedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: start this RPC server .
		for (int i = 0; i < DEFAULT_WORKER_COUNT; i++) {
			CallWorker callWorker = new CallWorker(this);
			callWorker.start();
		}
	}

	public void registerService(String serviceName, RpcService serviceInstance)
			throws ServiceAlreadyExistedException {
		synchronized (services) {
			if (services.containsKey(serviceName))
				throw new ServiceAlreadyExistedException(serviceName);
			Service service = new Service(serviceName, serviceInstance);
			services.put(serviceName, service);
		}
	}

	private Service getService(String serviceName)
			throws ServiceNotFoundException {
		Service service = services.get(serviceName);
		if (service == null)
			throw new ServiceNotFoundException(serviceName + " not found");
		return service;
	}

	@Override
	public ServiceDescription[] getServices() throws RpcException {
		List<ServiceDescription> list = new ArrayList<ServiceDescription>();
		for (Entry<String, Service> entry : services.entrySet()) {
			list.add(new ServiceDescriptionImpl(entry.getKey(), entry
					.getValue().getProtocols()));
		}
		return list.toArray(new ServiceDescriptionImpl[0]);
	}

	public void invokeCall(ServerCall call) {
		Invocation invocation = call.getInvocation();
		RpcService serviceObject;
		try {
			serviceObject = getService(invocation.getServiceName())
					.getInstance();
			Object result = invocation.invoke(serviceObject);
			call.setResult(result);
		} catch (ServiceNotFoundException e) {
			call.setException(true, e);
		} catch (UndeclaredThrowableException e) {
			// Log the error.
			e.printStackTrace();
			call.setException(true, e.getUndeclaredThrowable());
		} catch (Throwable t) {
			call.setException(false, t);
		}

	}

	@Override
	public ServiceDescription getDescription(String serviceName)
			throws RpcException, ServiceNotFoundException {
		ServiceDescriptionImpl serviceDescription = new ServiceDescriptionImpl(
				serviceName, getService(serviceName).getProtocols());
		return serviceDescription;

	}

	public static void main(String[] args) throws IOException {

		RpcServer server = new RpcServer(PORT_NUMBER);
		server.start();
	}

}