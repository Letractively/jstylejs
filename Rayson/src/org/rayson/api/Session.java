package org.rayson.api;

import java.net.SocketAddress;

public interface Session {

	public long getId();

	public long getCreationTime();

	public long getInvocationTime();

	public byte getVersion();

	public String getServiceName();

	public SocketAddress getPeerAddress();
}
