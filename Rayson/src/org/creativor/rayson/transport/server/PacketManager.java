/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.creativor.rayson.transport.common.Packet;

class PacketManager {

	private LinkedBlockingQueue<ConnectionPacketLink> receives;

	PacketManager() {

		receives = new LinkedBlockingQueue<ConnectionPacketLink>();
	}

	public void addReceived(RpcConnection connection, Packet packet) {
		receives.add(new ConnectionPacketLink(connection, packet));
	}

	public ConnectionPacketLink takeReceived() throws InterruptedException {
		return receives.take();
	}
}