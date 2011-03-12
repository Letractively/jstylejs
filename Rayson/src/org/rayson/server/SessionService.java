package org.rayson.server;

import java.net.SocketAddress;

import org.rayson.api.RpcService;
import org.rayson.common.ClientInfo;
import org.rayson.server.api.RpcSession;

public interface SessionService extends RpcService {
	public long logIn(ClientInfo clientInfo, SocketAddress remoteAddr);

	public void logOut(RpcSession session);
}
