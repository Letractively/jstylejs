package org.rayson.api;

public interface Session {

	public long getId();

	public long getCreationTime();

	public long getLastInvocationTime();

	public byte getProtocol();

	public String getServiceName();
}
