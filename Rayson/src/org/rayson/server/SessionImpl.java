package org.rayson.server;

import org.rayson.api.Session;

final class SessionImpl implements Session {
	private long id;

	public SessionImpl(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}
}
