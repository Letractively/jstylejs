package org.rayson.server;

import java.net.SocketAddress;

import org.rayson.common.ClientInfo;
import org.rayson.server.api.RpcSession;

public interface SessionFactory {
	public abstract RpcSession get(long sessionId);

	public abstract RpcSession create(ClientInfo clientInfo,
			SocketAddress remoteAddr);

	public void abandon(long sessionId);
}
