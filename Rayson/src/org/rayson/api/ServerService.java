package org.rayson.api;

public interface ServerService extends RpcService {

	public ServiceRegistration[] list() throws RemoteException;

	public ServiceRegistration find(String serviceName)
			throws ServiceNotFoundException, RemoteException;

}
