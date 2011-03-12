package org.rayson.server;

import java.net.SocketAddress;

import org.rayson.common.ClientInfo;
import org.rayson.server.api.RpcSession;

class NullSessionFactory implements SessionFactory {
	@Override
	public RpcSession get(long sessionId) {
		return null;
	}

	@Override
	public RpcSession create(ClientInfo clientInfo, SocketAddress remoteAddr) {
		return null;
	}

	@Override
	public void abandon(long sessionId) {
		return;
	}
}
