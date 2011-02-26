package org.rayson.api;

public interface ServerService extends RpcService {

	public ServiceDescription[] listServices() throws RpcException;

	public ServiceDescription getDescription(String serviceName)
			throws RpcException, ServiceNotFoundException;

}
