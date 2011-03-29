package org.creativor.rayson.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.ServiceRegistration;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.common.Invocation;
import org.creativor.rayson.common.InvocationException;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.impl.ServiceDescriptionImpl;
import org.creativor.rayson.transport.api.ServiceAlreadyExistedException;
import org.creativor.rayson.transport.server.TransportServerImpl;

public class RpcServer extends TransportServerImpl implements ServerService {

	private HashMap<String, ServiceReflection> services;

	protected RpcServer() {
		super();
		services = new HashMap<String, ServiceReflection>();
	}

	@Override
	public ServiceRegistration find(Session session, String serviceName)
			throws ServiceNotFoundException {
		ServiceReflection service = getService(serviceName);
		ServiceDescriptionImpl serviceDescription = new ServiceDescriptionImpl(
				service.getName(), service.getDescription(),
				service.getProxys());
		return serviceDescription;

	}

	private ServiceReflection getService(String serviceName)
			throws ServiceNotFoundException {
		ServiceReflection service = services.get(serviceName);
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
		ServiceReflection serviceObject;

		Session session = call.getSession();
		try {

			serviceObject = getService(session.getServiceName());
			result = serviceObject.invoke(call.getSession(), invocation);

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
		for (Entry<String, ServiceReflection> entry : services.entrySet()) {
			ServiceReflection serviceReflection = entry.getValue();
			list.add(new ServiceDescriptionImpl(serviceReflection.getName(),
					serviceReflection.getDescription(), serviceReflection
							.getProxys()));
		}
		return list.toArray(new ServiceDescriptionImpl[0]);
	}

	public void registerService(String serviceName, String description,
			RpcService serviceInstance) throws ServiceAlreadyExistedException,
			IllegalServiceException {
		synchronized (services) {
			if (services.containsKey(serviceName))
				throw new ServiceAlreadyExistedException(serviceName);
			ServiceReflection service = new ServiceReflection(serviceName,
					description, serviceInstance);
			services.put(serviceName, service);
		}
	}

	@Override
	public void start() throws IOException {
		super.start();
		// Register it self as a service.
		try {
			this.registerService(ServerService.NAME, ServerService.DESCRIPTION,
					this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (int i = 0; i < this.getConfig().workerCount(); i++) {
			CallWorker callWorker = new CallWorker(this);
			callWorker.start();
		}
	}

	@Override
	public String getServerInfo(Session session) {
		return "Rpc server";
	}
}