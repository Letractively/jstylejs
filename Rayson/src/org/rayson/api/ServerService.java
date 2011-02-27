package org.rayson.api;

public interface ServerService extends RpcService {

	public ServiceRegistration[] getServices() throws RpcException;

	public ServiceRegistration getRegistration(String serviceName)
			throws RpcException, ServiceNotFoundException;

}
