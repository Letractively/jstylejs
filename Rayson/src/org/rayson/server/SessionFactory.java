package org.rayson.server;

import org.rayson.api.Session;

public abstract class SessionFactory {
	public abstract Session getSession(long sessionId);

	private static SessionFactory theFactory;

	public static SessionFactory getDefault() {
		synchronized (SessionFactory.class) {
			if (theFactory == null)
				theFactory = new DefaultSessionFactory();
		}
		return theFactory;
	}
}
