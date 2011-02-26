package org.rayson.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.rayson.RpcException;
import org.rayson.RpcService;
import org.rayson.ServiceDescription;
import org.rayson.io.Invocation;
import org.rayson.transport.server.TransportServerImpl;

class RpcServer extends TransportServerImpl implements ServerService {
	private static final String DEFAULT_SERVICE_NAME = "server";
	private static final int DEDAULT_WORKER_COUNT = 4;
	private ConcurrentHashMap<String, RpcService> services;

	RpcServer(int portNum) {
		super(portNum);
		services = new ConcurrentHashMap<String, RpcService>();
	}

	@Override
	public void start() throws IOException {
		super.start();
		// Register it self as a service.
		try {
			this.registerService(DEFAULT_SERVICE_NAME, this);
		} catch (ServiceExistedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: start this RPC server .
		for (int i = 0; i < DEDAULT_WORKER_COUNT; i++) {
			CallWorker callWorker = new CallWorker(this);
			callWorker.start();
		}
	}

	public void registerService(String serviceName, RpcService serviceInstance)
			throws ServiceExistedException {
		if (services.putIfAbsent(serviceName, serviceInstance) != null)
			throw new ServiceExistedException(serviceName);
	}

	RpcService getService(String serviceName) throws ServiceNotFoundException {
		RpcService service = services.get(serviceName);
		if (service == null)
			throw new ServiceNotFoundException(serviceName);
		return service;
	}

	@Override
	public ServiceDescription[] listServices() throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	public void invokeCall(ServerCall call) {
		Invocation invocation = call.getInvocation();
		RpcService serviceObject;
		try {
			serviceObject = getService(invocation.getServiceName());
			Object result = invocation.invoke(serviceObject);
			call.setResult(result);
		} catch (Throwable t) {
			t.printStackTrace();
			call.setException(t);
		}

	}

	@Override
	public ServiceDescription getDescription(String serviceName)
			throws RpcException {
		try {
			RpcService rpcService = getService(serviceName);
			ServiceDescription serviceDescription = new ServiceDescription(
					serviceName, rpcService.getClass());
			return serviceDescription;
		} catch (ServiceNotFoundException e) {
			return null;
		}
	}

	public static void main(String[] args) throws IOException {

		RpcServer server = new RpcServer(PORT_NUMBER);
		server.start();
	}

}