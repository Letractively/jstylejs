package org.rayson.server.api;

import java.net.SocketAddress;

import org.rayson.common.ClientInfo;

public interface SessionFactory {
	public abstract RpcSession get(long sessionId);

	public abstract RpcSession create(ClientInfo clientInfo,
			SocketAddress remoteAddr);

	public void abandon(long sessionId);
}
