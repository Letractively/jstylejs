package org.creativor.viva.api;

import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;

@Proxy(VivaProxy.class)
public interface VivaService extends RpcService {
	public void join(Session session, int hashCode);

	public Card getCard(Session session, int hashCode);

	public void putCard(Session session, int hashCode, Portable value);

}
