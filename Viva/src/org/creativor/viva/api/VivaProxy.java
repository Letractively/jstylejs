/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.api;

import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.exception.RpcException;

public interface VivaProxy extends RpcProxy {

	public boolean join(int jioner, int port) throws RpcException,
			HashCodeCollisionException;

	public int getId() throws RpcException;

	public PortableStaff[] list() throws RpcException;

	/**
	 * 
	 * @param joiner
	 * @param leftDirection
	 *            True if the direction is left.
	 * 
	 * @return
	 * @throws RpcException
	 */
	public boolean notifyJoin(int joiner, String ip, int port,
			boolean leftDirection) throws RpcException;

	public String pictureStaffs() throws RpcException;

}