/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.server;

import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.exception.UnsupportedVersionException;

/**
 *
 * @author Nick Zhang
 */
public interface RpcSession extends Session {
	public void checkProxyVersion(RpcService service)
			throws UnsupportedVersionException;
}