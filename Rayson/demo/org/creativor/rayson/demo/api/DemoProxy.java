/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.demo.api;

import org.creativor.rayson.annotation.ClientVersion;
import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.exception.RpcException;

/**
 * 
 * @author Nick Zhang
 */
@ClientVersion(10)
public interface DemoProxy extends RpcProxy {

	public String echo(String message) throws NullPointerException,
			RpcException;

	public int getInt() throws RpcException;

	public int[] getIntArray() throws RpcException;

	public Integer getInteger() throws RpcException;

	public void voidMethod() throws RpcException;
}
