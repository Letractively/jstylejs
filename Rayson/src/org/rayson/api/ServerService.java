package org.rayson.api;


public interface ServerService extends RpcService {

	public ServiceRegistration[] list();

	public ServiceRegistration find(String serviceName)
			throws ServiceNotFoundException;

}
