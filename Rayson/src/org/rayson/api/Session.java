package org.rayson.api;

public interface Session {

	public long getId();

	public long getCreationTime();

	public long getInvocationTime();

	public byte getVersion();

	public String getServiceName();
}
