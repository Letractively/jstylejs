package org.rayson.server.api;

import java.net.SocketAddress;

public interface RpcSession {
	public long getId();

	public long getCreationTime();

	public long getLastAccessedTime();

	public boolean isNew();

	public byte getProtocol();

	public void invalidate();

	public SocketAddress getRemoteAddr();
}