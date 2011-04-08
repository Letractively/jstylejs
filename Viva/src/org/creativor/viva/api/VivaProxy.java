package org.creativor.viva.api;

import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.exception.RpcException;

public interface VivaProxy extends RpcProxy {

	public boolean join(int joiner) throws RpcException;

	public int getId() throws RpcException;

	/**
	 * 
	 * @param joiner
	 * @param leftDirection
	 *            True if the direction is left.
	 * 
	 * @return
	 * @throws RpcException
	 */
	public boolean notifyJoin(int joiner, String ip, short port,
			boolean leftDirection) throws RpcException;

}