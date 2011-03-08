package org.rayson.server;

import org.rayson.api.Session;

public interface SessionFactory {
	public abstract Session getSession(long sessionId);
}
