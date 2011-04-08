package org.creativor.viva.api;

import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;

@Proxy(VivaProxy.class)
public interface VivaService extends RpcService {

	/**
	 * Notify the left and right staff that one new staff has joining into the
	 * Viva system.
	 * 
	 * @param session
	 * @param jioner
	 * @return
	 */
	public boolean join(Session session, int jioner);

	public int getId(Session session);

	/**
	 * Notify that one staff is joining into the Viva system.
	 * 
	 * @param session
	 * @param joiner
	 * @param ip TODO
	 * @param port TODO
	 * @param leftDirection
	 *            True if the direction is left.
	 * @return
	 */
	public boolean notifyJoin(Session session, int joiner, String ip, short port, boolean leftDirection);

}