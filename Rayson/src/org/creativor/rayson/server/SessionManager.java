package org.creativor.rayson.server;

import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.common.ClientSession;
import org.creativor.rayson.server.impl.RpcSessionImpl;

final class SessionManager {
	private static SessionManager singleton = new SessionManager();

	public static SessionManager getSingleton() {
		return singleton;
	}

	public RpcSession getRpcSession(ClientSession clientSession,
			RpcService service) {
		return new RpcSessionImpl(clientSession, service);
	}
}
