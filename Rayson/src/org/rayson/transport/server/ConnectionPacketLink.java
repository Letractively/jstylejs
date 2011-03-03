package org.rayson.transport.server;

import org.rayson.transport.common.Packet;

class ConnectionPacketLink {
	private RpcServerConnection connection;
	private Packet packet;

	ConnectionPacketLink(RpcServerConnection connection, Packet packet) {
		this.connection = connection;
		this.packet = packet;
	}

	public RpcServerConnection getConnection() {
		return connection;
	}

	public Packet getPacket() {
		return packet;
	}

}
