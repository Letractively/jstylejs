package org.rayson.server.impl;

import java.net.SocketAddress;

import org.rayson.common.ClientSession;
import org.rayson.server.RpcSession;

public final class RpcSessionImpl implements RpcSession {
	private SocketAddress remoteAddr;
	private ClientSession clientSession;

	public RpcSessionImpl(ClientSession clientSession, SocketAddress remoteAddr) {
		this.clientSession = clientSession;
		this.remoteAddr = remoteAddr;
	}

	@Override
	public long getId() {
		return clientSession.getId();
	}

	@Override
	public long getCreationTime() {
		return clientSession.getCreationTime();
	}

	@Override
	public long getLastInvocationTime() {
		return clientSession.getLastInvocationTime();
	}

	@Override
	public byte getProtocol() {
		return clientSession.getProtocol();
	}

	@Override
	public SocketAddress getRemoteAddr() {
		return remoteAddr;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("Client session: ");
		sb.append(clientSession.toString());
		sb.append(", remote addr: ");
		sb.append(this.remoteAddr.toString());
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String getServiceName() {
		return clientSession.getServiceName();
	}
}