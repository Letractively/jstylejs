package org.rayson.api;

import org.rayson.exception.RemoteException;
import org.rayson.exception.ServiceNotFoundException;

public interface ServerService extends RpcService {

	public ServiceRegistration[] list() throws RemoteException;

	public ServiceRegistration find(String serviceName)
			throws ServiceNotFoundException, RemoteException;

}
