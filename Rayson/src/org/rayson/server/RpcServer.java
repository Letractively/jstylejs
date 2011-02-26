package org.rayson.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.rayson.RpcException;
import org.rayson.RpcService;
import org.rayson.ServiceDescription;
import org.rayson.transport.server.TransportServerImpl;

class RpcServer extends TransportServerImpl implements ServerService {

	private static final int DEDAULT_WORKER_COUNT = 4;
	private ConcurrentHashMap<String, RpcService> services;

	RpcServer(int portNum) {
		super(portNum);
		services = new ConcurrentHashMap<String, RpcService>();
	}

	@Override
	public void start() throws IOException {
		super.start();
		// TODO: start this RPC server .
		for (int i = 0; i < DEDAULT_WORKER_COUNT; i++) {
			CallWorker callWorker = new CallWorker(this);
			callWorker.start();
		}
	}

	public void registerService(String serviceName, RpcService serviceInstance)
			throws ServiceExistedException {
		if (services.putIfAbsent(serviceName, serviceInstance) == null)
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

	@Override
	public ServiceDescription getDescription(String serviceName)
			throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

}
