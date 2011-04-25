/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.demo.server;

import java.io.EOFException;
import org.creativor.rayson.annotation.AsyncProxy;
import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.demo.api.DemoAsyncProxy;
import org.creativor.rayson.demo.api.DemoProxy;

/**
 * 
 * @author Nick Zhang
 */
@AsyncProxy(DemoAsyncProxy.class)
@Proxy(DemoProxy.class)
public interface DemoService extends RpcService {
	public String echo(Session session, String message)
			throws NullPointerException;

	public int getInt(Session session);

	public int[] getIntArray(Session session);

	public Integer getInteger(Session session);

	public void voidMethod(Session session);
}
