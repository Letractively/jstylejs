package org.rayson.server.impl;

import java.net.SocketAddress;
import java.util.HashMap;

import org.rayson.common.ClientInfo;
import org.rayson.server.api.RpcSession;
import org.rayson.server.api.SessionFactory;

public class DefaultSessionFactory implements SessionFactory {
	private HashMap<Long, RpcSession> sessions;

	public DefaultSessionFactory() {
		this.sessions = new HashMap<Long, RpcSession>();
	}

	@Override
	public RpcSession get(long sessionId) {
		return this.sessions.get(sessionId);
	}

	private long createNewId() {
		return System.nanoTime();
	}

	@Override
	public RpcSession create(ClientInfo clientInfo, SocketAddress remoteAddr) {
		long newSessionId = createNewId();
		RpcSession session = new SessionImpl(newSessionId, clientInfo,
				remoteAddr, this);
		this.sessions.put(newSessionId, session);
		return session;
	}

	public void abandon(long sessionId) {
		this.sessions.remove(sessionId);
	}
}