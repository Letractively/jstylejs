/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.api;

import org.creativor.rayson.exception.UnsupportedVersionException;

/**
 *
 * @author Nick Zhang
 */
public interface RpcService {
	/**
	 * Check whether the proxy version is supported by this service. If the
	 * proxy version is unsupported by this service, the the rpc call invoked by
	 * the client proxy will catch a {@link UnsupportedVersionException}. <br/>
	 * Once this method return false, the rpc invocations of associated client
	 * proxy will always throws {@link UnsupportedVersionException}.
	 * 
	 * @param session
	 *            Session information.
	 * @return True is the proxy version is supported by this service.
	 */
	public boolean isSupportedVersion(Session session);
}
