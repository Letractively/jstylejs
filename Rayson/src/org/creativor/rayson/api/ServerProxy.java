/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.api;

import org.creativor.rayson.annotation.ClientVersion;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.exception.ServiceNotFoundException;

/**
 *
 * @author Nick Zhang
 */
@ClientVersion(1)
public interface ServerProxy extends RpcProxy {

	public ServiceRegistration find(String serviceName)
			throws ServiceNotFoundException, RpcException;

	public ServiceRegistration[] list() throws RpcException;

	public String getServerInfo() throws RpcException;

}
