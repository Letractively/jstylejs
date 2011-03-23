package org.rayson.demo.explorer.server;

import org.rayson.annotation.Proxy;
import org.rayson.api.RpcService;
import org.rayson.api.Session;
import org.rayson.demo.explorer.api.ExplorerProxy;

@Proxy(ExplorerProxy.class)
interface ExplorerService extends RpcService {
	public String[] list(Session session, String path);
}
