package org.rayson.api;

public interface ServerService extends RpcService {

	public ServiceRegistration[] list() throws RpcException;

	public ServiceRegistration find(String serviceName) throws RpcException,
			ServiceNotFoundException;

}
