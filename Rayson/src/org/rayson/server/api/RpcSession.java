package org.rayson.server.api;

import java.net.SocketAddress;

import org.rayson.api.Session;

public interface RpcSession extends Session {
	public SocketAddress getRemoteAddr();
}