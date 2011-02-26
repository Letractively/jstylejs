package org.rayson.transport.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.rayson.transport.common.Packet;

class PacketManager {

	private LinkedBlockingQueue<ConnectionPacketLink> receives;

	PacketManager() {

		receives = new LinkedBlockingQueue<ConnectionPacketLink>();
	}

	public void addReceived(ServerConnection connection, Packet packet) {
		receives.add(new ConnectionPacketLink(connection, packet));
	}

	public ConnectionPacketLink takeReceived() throws InterruptedException {
		return receives.take();
	}
}