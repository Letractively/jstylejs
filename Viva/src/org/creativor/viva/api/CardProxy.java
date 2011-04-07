package org.creativor.viva.api;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.exception.RpcException;

public interface CardProxy extends RpcProxy {

	public Portable get(int hashCode) throws RpcException;

	public void put(int hashCode, Portable value) throws RpcException;

}
