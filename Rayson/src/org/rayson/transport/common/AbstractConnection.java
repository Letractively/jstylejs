package org.rayson.transport.common;

import java.io.IOException;

public abstract class AbstractConnection implements Connection {

	protected AbstractConnection() {
		lastContact = System.currentTimeMillis();
	}

	private ProtocolType protocol = ProtocolType.RPC;

	private volatile long lastContact;

	public abstract void init() throws IOException;

	public abstract int pendingPacketCount();

	public abstract void addSendPacket(Packet responsePacket)
			throws IOException;

	public final long getLastContact() {
		return lastContact;
	}

	public final void touch() {
		this.lastContact = System.currentTimeMillis();
	}

	@Override
	public final ProtocolType getProtocol() {
		return protocol;
	}

	public abstract int getVersion();

	public abstract boolean isTimeOut();

}