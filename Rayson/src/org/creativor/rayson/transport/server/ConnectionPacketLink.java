/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.server;

import org.creativor.rayson.transport.common.Packet;

/**
 *
 * @author Nick Zhang
 */
class ConnectionPacketLink {
	private RpcConnection connection;
	private Packet packet;

	ConnectionPacketLink(RpcConnection connection, Packet packet) {
		this.connection = connection;
		this.packet = packet;
	}

	public RpcConnection getConnection() {
		return connection;
	}

	public Packet getPacket() {
		return packet;
	}

}
