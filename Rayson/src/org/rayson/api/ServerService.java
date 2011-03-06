package org.rayson.api;

import org.rayson.exception.RemoteException;
import org.rayson.exception.ServiceNotFoundException;

public interface ServerService extends RpcService {

	public static final String NAME = "server";

	public ServiceRegistration find(String serviceName)
			throws ServiceNotFoundException, RemoteException;

	public ServiceRegistration[] list() throws RemoteException;

}
