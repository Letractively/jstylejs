package org.rayson.transport.common;

import java.io.IOException;

import org.rayson.transport.api.TimeLimitConnection;

public abstract class PacketConnection extends TimeLimitConnection {

	private ProtocolType protocol = ProtocolType.RPC;

	protected PacketConnection() {
	}

	public abstract void addSendPacket(Packet responsePacket)
			throws IOException;

	@Override
	public final ProtocolType getProtocol() {
		return protocol;
	}

	public abstract void init() throws IOException;

	public abstract int pendingPacketCount();

}