package org.rayson.server;

import org.rayson.annotation.Protocols;
import org.rayson.api.RpcService;
import org.rayson.api.ServerProtocol;
import org.rayson.api.ServiceRegistration;
import org.rayson.exception.RpcException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.server.api.RpcSession;

@Protocols(ServerProtocol.class)
interface ServerService extends RpcService {

	public static final String DESCRIPTION = "Rpc server default service";

	public ServiceRegistration find(RpcSession session, String serviceName)
			throws ServiceNotFoundException, RpcException;

	public ServiceRegistration[] list(RpcSession session) throws RpcException;

	public String getServerInfo(RpcSession session) throws RpcException;

}