/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.api;

import org.creativor.rayson.api.AsyncProxy;
import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.exception.NetWorkException;

/**
 *
 * @author Nick Zhang
 */
public interface TestAsyncProxy extends AsyncProxy {

	public CallFuture<String> echo(String message) throws NetWorkException;

	public CallFuture<Integer> getInt() throws NetWorkException;

	public CallFuture<int[]> getIntArray() throws NetWorkException;

	public CallFuture<Integer> getInteger() throws NetWorkException;

	public CallFuture<Void> voidMethod() throws NetWorkException;
}
