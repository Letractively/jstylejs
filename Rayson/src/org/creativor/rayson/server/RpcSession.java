package org.creativor.rayson.server;

import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.exception.UnsupportedVersionException;

public interface RpcSession extends Session {
	public void checkProxyVersion(RpcService service)
			throws UnsupportedVersionException;
}