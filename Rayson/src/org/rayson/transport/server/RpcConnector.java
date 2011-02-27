package org.rayson.transport.server;

import java.io.IOException;
import java.util.HashMap;

import org.rayson.server.ServerCall;
import org.rayson.transport.common.Packet;

public class RpcConnector {

	private HashMap<Long, ServerConnection> callConenctions;
	private TransportServer server;

	RpcConnector(TransportServer server) {
		this.server = server;
		callConenctions = new HashMap<Long, ServerConnection>();
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
		try {
			serverCall = ServerCall.fromPacket(connectionPacket.getPacket());
		} catch (IOException e) {
			// TODO: handling this exception
			e.printStackTrace();
		}
		callConenctions.put(serverCall.getId(),
				connectionPacket.getConnection());
		return serverCall;
	}
}
