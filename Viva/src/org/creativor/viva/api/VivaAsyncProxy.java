package org.creativor.viva.api;

import org.creativor.rayson.api.AsyncProxy;
import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.exception.NetWorkException;

public interface VivaAsyncProxy extends AsyncProxy {

	public CallFuture<Boolean> join(int jioner, int port)
			throws NetWorkException;

	public CallFuture<Integer> getId() throws NetWorkException;

	public CallFuture<PortableStaff[]> list() throws NetWorkException;

	/**
	 * 
	 * @param joiner
	 * @param leftDirection
	 *            True if the direction is left.
	 * 
	 * @return
	 * @throws NetWorkException
	 */
	public CallFuture<Boolean> notifyJoin(int joiner, String ip, int port,
			boolean leftDirection) throws NetWorkException;

	public CallFuture<String> pictureStaffs() throws NetWorkException;
}