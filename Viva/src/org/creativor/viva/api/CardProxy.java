/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.api;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.exception.RpcException;

/**
 *
 * @author Nick Zhang
 */
public interface CardProxy extends RpcProxy {

	public Portable get(int hashCode) throws RpcException;

	public void put(int hashCode, Portable value) throws RpcException;

}
