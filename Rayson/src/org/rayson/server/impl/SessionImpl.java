package org.rayson.server.impl;

import java.net.SocketAddress;

import org.rayson.common.ClientInfo;
import org.rayson.server.api.RpcSession;
import org.rayson.server.api.SessionFactory;

public final class SessionImpl implements RpcSession {
	private long id;
	private byte protocol;
	private long creationTime;
	private boolean isNew;
	private SocketAddress remoteAddr;
	private long lastAccessedTime;
	private SessionFactory factory;

	public SessionImpl(long id, ClientInfo clientInfo,
			SocketAddress remoteAddr, SessionFactory factory) {
		this.id = id;
		this.protocol = clientInfo.getProtocol();
		this.creationTime = System.currentTimeMillis();
		this.isNew = true;
		this.remoteAddr = remoteAddr;
		this.factory = factory;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}

	@Override
	public byte getProtocol() {
		return protocol;
	}

	@Override
	public void invalidate() {
		this.factory.abandon(id);
	}

	@Override
	public SocketAddress getRemoteAddr() {
		return remoteAddr;
	}

	public void touch() {
		this.isNew = false;
		this.lastAccessedTime = System.currentTimeMillis();
	}
}
