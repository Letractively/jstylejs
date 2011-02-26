package org.rayson.transport.server;

import org.rayson.transport.common.Packet;

class ConnectionPacket {
	private ServerConnection connection;
	private Packet packet;

	ConnectionPacket(ServerConnection connection, Packet packet) {
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
