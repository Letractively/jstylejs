package org.rayson.server;

import org.rayson.annotation.RpcProtocols;
import org.rayson.api.RpcService;
import org.rayson.api.ServerProtocol;
import org.rayson.api.ServiceRegistration;
import org.rayson.api.Session;
import org.rayson.exception.RemoteException;
import org.rayson.exception.ServiceNotFoundException;

@RpcProtocols(ServerProtocol.class)
interface ServerService extends RpcService {
	public ServiceRegistration find(Session session, String serviceName)
			throws ServiceNotFoundException, RemoteException;

	public ServiceRegistration[] list(Session session) throws RemoteException;
}
