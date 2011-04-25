/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.demo.api;

import org.creativor.rayson.api.AsyncRpcProxy;
import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.exception.NetWorkException;

/**
 * 
 * @author Nick Zhang
 */
public interface DemoAsyncProxy extends AsyncRpcProxy {

	public CallFuture<String> echo(String message) throws NetWorkException;

	public CallFuture<Integer> getInt() throws NetWorkException;

	public CallFuture<int[]> getIntArray() throws NetWorkException;

	public CallFuture<Integer> getInteger() throws NetWorkException;

	public CallFuture<Void> voidMethod() throws NetWorkException;
}
