package org.creativor.rayson.server.impl;

import java.net.InetSocketAddress;

import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.common.ClientSession;
import org.creativor.rayson.exception.UnsupportedVersionException;
import org.creativor.rayson.server.RpcSession;

public final class RpcSessionImpl implements RpcSession {
	private ClientSession clientSession;
	private boolean proxyVersionSupported = false;

	public RpcSessionImpl(ClientSession clientSession, RpcService service) {
		this.clientSession = clientSession;
		proxyVersionSupported = service.isSupportedVersion(clientSession);
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
	public InetSocketAddress getPeerAddress() {
		return clientSession.getPeerAddress();
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
		sb.append("proxy version: ");
		sb.append(getProxyVersion());
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

	@Override
	public short getProxyVersion() {
		return clientSession.getProxyVersion();
	}

	/**
	 * Check remote RPC proxy version.<br/>
	 * 
	 * @param service
	 * @throws UnsupportedVersionException
	 */
	public void checkProxyVersion(RpcService service)
			throws UnsupportedVersionException {
		if (!proxyVersionSupported)
			throw new UnsupportedVersionException("Proxy version "
					+ this.getProxyVersion()
					+ " is unsupported by server service");
	}
}