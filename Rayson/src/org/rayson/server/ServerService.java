package org.rayson.server;

import org.rayson.RpcException;
import org.rayson.RpcService;
import org.rayson.ServiceDescription;

public interface ServerService extends RpcService {

	public ServiceDescription[] listServices() throws RpcException;

	public ServiceDescription getDescription(String serviceName)
			throws RpcException;

}
