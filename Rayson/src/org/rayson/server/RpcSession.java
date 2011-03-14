package org.rayson.server;

import java.net.SocketAddress;

import org.rayson.api.Session;

public interface RpcSession extends Session {
	public SocketAddress getRemoteAddr();
}