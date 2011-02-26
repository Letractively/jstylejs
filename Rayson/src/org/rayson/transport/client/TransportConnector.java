package org.rayson.transport.client;

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

	public void returnCall(Packet responsePacket) {
		// TODO:
	}

	public void sumbitCall(SocketAddress serverAddress, ClientCall call)
			throws ConnectException, IOException {
		ClientConnection connection = client.getConnection(serverAddress);
		connection.addSendPacket(call.getRequestPacket());
		CallWrapper callWrapper = new CallWrapper(connection, call);
		this.calls.put(call.getId(), callWrapper);
	}
}
