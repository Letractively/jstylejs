package org.creativor.viva.api;

import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.exception.RpcException;

public interface VivaProxy extends RpcProxy {

	public boolean join(int hashCode) throws RpcException;

	public int getId() throws RpcException;

	public void notifyJoin(int hashCode) throws RpcException;

}
