/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.api;

import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;

/**
 *
 * @author Nick Zhang
 */
@Proxy(VivaProxy.class)
public interface VivaService extends RpcService {

	/**
	 * @param session
	 * @param jioner
	 * @return
	 */
	public boolean join(Session session, int jioner, int port)
			throws HashCodeCollisionException;

	public int getId(Session session);

	public PortableStaff[] list(Session session);

	/**
	 * Notify that one staff is joining into the Viva system.
	 * 
	 * @param session
	 * @param joiner
	 * @param ip
	 *            TODO
	 * @param port
	 *            TODO
	 * @param leftDirection
	 *            True if the direction is left.
	 * @return
	 */
	public boolean notifyJoin(Session session, int joiner, String ip, int port,
			boolean leftDirection);

	/**
	 * Return a string that picture all the staffs of the Viva system in this
	 * peer.<br>
	 * The string looks like that:<br>
	 * |->first staff id-> next staff id -> next staff id* -> last staff id ->| <br>
	 * Where * indicate that it's the current staff id.
	 * 
	 * @param session
	 * @return
	 */
	public String pictureStaffs(Session session);

}