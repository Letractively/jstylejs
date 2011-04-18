package org.creativor.rayson.client;

import java.net.InetSocketAddress;

import org.creativor.rayson.common.ClientSession;
import org.creativor.rayson.exception.UnsupportedVersionException;

final class ProxySession extends ClientSession {

	private volatile boolean unsupportedVersion = false;
	private String unsupportedVersionMsg;

	public ProxySession(byte version, short proxyVersion, long sessionId,
			String serviceName, long creationTime, InetSocketAddress peerAddress) {
		super(version, proxyVersion, sessionId, serviceName, creationTime,
				peerAddress);
	}

	public void getUnsupportedVersionException(String exceptionMsg) {
		unsupportedVersion = true;
		unsupportedVersionMsg = exceptionMsg;
	}

	public boolean isUnsupportedVersion() {
		return unsupportedVersion;
	}

	public UnsupportedVersionException getUnsupportedVersionException() {
		if (unsupportedVersion)
			return new UnsupportedVersionException(unsupportedVersionMsg);
		return null;
	}
}
