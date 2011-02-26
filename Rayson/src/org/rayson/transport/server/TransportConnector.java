package org.rayson.transport.server;

import java.io.IOException;
import java.util.HashMap;

import org.rayson.server.ServerCall;
import org.rayson.transport.common.Packet;

public class TransportConnector {

	private HashMap<Long, ServerConnection> callConenctions;
	private TransportServer server;

	TransportConnector(TransportServer server) {
		this.server = server;
	}

	public void returnCall(long callId, Packet responsePacket)
			throws IOException {
		ServerConnection serverConnection = this.callConenctions.remove(callId);
		serverConnection.addSendPacket(responsePacket);
	}

	public ServerCall takeCall() throws InterruptedException {
		ConnectionPacketLink connectionPacket = this.server.getPacketManager()
				.takeReceived();
		ServerCall serverCall = null;
		callConenctions.put(serverCall.getId(),
				connectionPacket.getConnection());
		return serverCall;
	}
}
