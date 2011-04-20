/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.api;

import org.creativor.rayson.api.AsyncRpcProxy;
import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.api.Portable;
import org.creativor.rayson.exception.NetWorkException;

/**
 *
 * @author Nick Zhang
 */
public interface CardAsyncProxy extends AsyncRpcProxy {

	public CallFuture<Portable> get(int hashCode) throws NetWorkException;

	public CallFuture<Void> put(int hashCode, Portable value)
			throws NetWorkException;

}
