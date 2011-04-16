package org.creativor.rayson.api;

import org.creativor.rayson.annotation.ProxyVersion;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.exception.ServiceNotFoundException;

@ProxyVersion(1)
public interface ServerProxy extends RpcProxy {

	public ServiceRegistration find(String serviceName)
			throws ServiceNotFoundException, RpcException;

	public ServiceRegistration[] list() throws RpcException;

	public String getServerInfo() throws RpcException;

}
