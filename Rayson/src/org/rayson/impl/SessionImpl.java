package org.rayson.impl;

import org.rayson.api.Session;

public final class SessionImpl implements Session {
	private long id;

	public SessionImpl(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}
}
