/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.server;

import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.ServerProxy;
import org.creativor.rayson.api.ServiceRegistration;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.exception.ServiceNotFoundException;

@Proxy(ServerProxy.class)
public interface ServerService extends RpcService {
	public static final String NAME = "server";
	public static final String DESCRIPTION = "Rpc server default service";

	public ServiceRegistration find(Session session, String serviceName)
			throws ServiceNotFoundException;

	public ServiceRegistration[] list(Session session);

	public String getServerInfo(Session session);

}