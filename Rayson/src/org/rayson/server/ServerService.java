package org.rayson.server;

import org.rayson.annotation.Proxy;
import org.rayson.api.RpcService;
import org.rayson.api.ServerProxy;
import org.rayson.api.ServiceRegistration;
import org.rayson.api.Session;
import org.rayson.exception.ServiceNotFoundException;

@Proxy(ServerProxy.class)
public interface ServerService extends RpcService {
	public static final String NAME = "server";
	public static final String DESCRIPTION = "Rpc server default service";

	public ServiceRegistration find(Session session, String serviceName)
			throws ServiceNotFoundException;

	public ServiceRegistration[] list(Session session);

	public String getServerInfo(Session session);

}