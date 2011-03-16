package org.rayson.api;

import org.rayson.exception.RpcException;
import org.rayson.exception.ServiceNotFoundException;

public interface ServerProxy extends RpcProxy {

	public ServiceRegistration find(String serviceName)
			throws ServiceNotFoundException, RpcException;

	public ServiceRegistration[] list() throws RpcException;

	public String getServerInfo() throws RpcException;

}
