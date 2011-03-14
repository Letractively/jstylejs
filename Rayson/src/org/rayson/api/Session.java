package org.rayson.api;

public interface Session {

	public long getId();

	public long getCreationTime();

	public long getLastAccessedTime();

	public byte getProtocol();
}
