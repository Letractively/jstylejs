package org.rayson.server;

import org.rayson.api.Session;
import org.rayson.impl.SessionImpl;

class SessionFactory {
	public static Session getSession(long sessionId) {
		return new SessionImpl(sessionId);
	}
}
