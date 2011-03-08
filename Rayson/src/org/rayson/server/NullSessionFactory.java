package org.rayson.server;

import org.rayson.api.Session;

class NullSessionFactory implements SessionFactory {

	@Override
	public Session getSession(long sessionId) {
		return null;
	}

}
