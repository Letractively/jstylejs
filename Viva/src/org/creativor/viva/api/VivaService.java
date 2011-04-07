package org.creativor.viva.api;

import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;

@Proxy(VivaProxy.class)
public interface VivaService extends RpcService {

	public void join(Session session, int hashCode);

	public int getId(Session session);

	public void notifyJoin(Session session, int hashCode);

}
