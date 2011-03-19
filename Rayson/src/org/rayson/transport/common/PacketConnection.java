package org.rayson.transport.common;

import java.io.IOException;

public abstract class PacketConnection implements Connection {

	private volatile long lastContact;

	private ProtocolType protocol = ProtocolType.RPC;

	protected PacketConnection() {
		lastContact = System.currentTimeMillis();
	}

	public abstract void addSendPacket(Packet responsePacket)
			throws IOException;

	public final long getLastContact() {
		return lastContact;
	}

	@Override
	public final ProtocolType getProtocol() {
		return protocol;
	}

	public abstract int getVersion();

	public abstract void init() throws IOException;


	public abstract int pendingPacketCount();

	public final void touch() {
		this.lastContact = System.currentTimeMillis();
	}

}