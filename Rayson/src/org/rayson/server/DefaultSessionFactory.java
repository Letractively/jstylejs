package org.rayson.server;

import org.rayson.api.Session;

class DefaultSessionFactory implements SessionFactory {
	@Override
	public Session getSession(long sessionId) {
		return new SessionImpl(sessionId);
	}
}