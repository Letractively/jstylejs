/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.server;

import java.io.EOFException;
import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.api.TestProxy;

/**
 *
 * @author Nick Zhang
 */
@Proxy(TestProxy.class)
public interface TestService extends RpcService {
	public String echo(Session session, String message) throws EOFException;

	public int getInt(Session session);

	public int[] getIntArray(Session session);

	public Integer getInteger(Session session);

	public void voidMethod(Session session);
}
