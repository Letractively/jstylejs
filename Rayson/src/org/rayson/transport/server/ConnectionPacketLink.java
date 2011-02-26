package org.rayson.transport.server;

import org.rayson.transport.common.Packet;

class ConnectionPacketLink {
	private ServerConnection connection;
	private Packet packet;

	ConnectionPacketLink(ServerConnection connection, Packet packet) {
		this.connection = connection;
		this.packet = packet;
	}

	public ServerConnection getConnection() {
		return connection;
	}

	public Packet getPacket() {
		return packet;
	}

}
