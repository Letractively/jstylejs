/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.api;

import java.io.EOFException;

import org.creativor.rayson.annotation.ClientVersion;
import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.exception.RpcException;

@ClientVersion(10)
public interface TestProxy extends RpcProxy {

	public String echo(String message) throws EOFException, RpcException;

	public int getInt() throws RpcException;

	public int[] getIntArray() throws RpcException;

	public Integer getInteger() throws RpcException;

	public void voidMethod() throws RpcException;
}
