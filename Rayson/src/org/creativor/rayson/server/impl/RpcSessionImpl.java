package org.creativor.rayson.server.impl;

import java.net.SocketAddress;

import org.creativor.rayson.api.Session;
import org.creativor.rayson.common.ClientSession;

public final class RpcSessionImpl implements Session {
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
	public long getInvocationTime() {
		return clientSession.getInvocationTime();
	}

	@Override
	public byte getVersion() {
		return clientSession.getVersion();
	}

	@Override
	public SocketAddress getPeerAddress() {
		return remoteAddr;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id: ");
		sb.append(getId());
		sb.append(", ");
		sb.append("version: ");
		sb.append(getVersion());
		sb.append(", ");
		sb.append("service name: ");
		sb.append(getServiceName());
		sb.append(", ");
		sb.append("creationTime: ");
		sb.append(getCreationTime());
		sb.append(", ");
		sb.append("invocation time: ");
		sb.append(getInvocationTime());
		sb.append(", ");
		sb.append("remote addr: ");
		sb.append(getPeerAddress());
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String getServiceName() {
		return clientSession.getServiceName();
	}
}