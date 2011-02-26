package org.rayson.transport.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;

import org.rayson.client.ClientCall;
import org.rayson.transport.common.Packet;

public class TransportConnector {

	private static class CallWrapper {
		private ClientCall call;
		private ClientConnection connection;

		public CallWrapper(ClientConnection connection, ClientCall call) {
			this.connection = connection;
			this.call = call;
		}
	}

	private HashMap<Long, CallWrapper> calls;
	private TransportClient client;

	TransportConnector(TransportClient client) {
		this.client = client;
		calls = new HashMap<Long, CallWrapper>();
	}

	public void notifyConnectionError(ClientConnection connection) {
		// TODO:
	}

	private static final int BUFFER_SIZE = 1024;

	public void responseCall(Packet responsePacket) {
		// TODO:
	}

	public void sumbitCall(SocketAddress serverAddress, ClientCall call)
			throws ConnectException, IOException {
		ClientConnection connection = client.getConnection(serverAddress);
		connection.addSendPacket(call.getRequestPacket());
		CallWrapper callWrapper = new CallWrapper(connection, call);
		this.calls.put(call.getId(), callWrapper);
	}

	public ClientCall responseCall() throws InterruptedException,
			IOException {
		Packet receivedPacket = this.client.getPacketManager().takeReceived();
		ClientCall call = fromReceivePacket(receivedPacket);
		return call;
	}

	private ClientCall fromReceivePacket(Packet receivedPacket)
			throws IOException {
		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(receivedPacket.getData()));
		long callId = inputStream.readLong();
		ClientCall call = calls.get(callId).call;
		call.readResult(inputStream);
		return call;
	}
}
