package org.creativor.rayson.api;

import java.net.InetSocketAddress;

public interface Session {

	public long getId();

	public long getCreationTime();

	public long getInvocationTime();

	public byte getVersion();

	public short getProxyVersion();

	public String getServiceName();

	public InetSocketAddress getPeerAddress();
}
