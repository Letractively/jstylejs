package org.rayson.transport.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.rayson.transport.common.Packet;

class PacketManager {

	private LinkedBlockingQueue<ConnectionPacket> receives;

	PacketManager() {

		receives = new LinkedBlockingQueue<ConnectionPacket>();
	}

	public void addReceived(ServerConnection connection, Packet packet) {
		receives.add(new ConnectionPacket(connection, packet));
	}

	public ConnectionPacket takeReceived() throws InterruptedException {
		return receives.take();
	}
}