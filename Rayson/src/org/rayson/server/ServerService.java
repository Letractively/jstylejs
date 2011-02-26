package org.rayson.server;

import org.rayson.api.RpcException;
import org.rayson.api.RpcService;
import org.rayson.api.ServiceDescription;

public interface ServerService extends RpcService {

	public ServiceDescription[] listServices() throws RpcException;

	public ServiceDescription getDescription(String serviceName)
			throws RpcException;

}
