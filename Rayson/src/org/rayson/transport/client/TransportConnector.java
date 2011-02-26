package org.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;

import org.rayson.client.ClientCall;
import org.rayson.transport.common.Packet;

public class TransportConnector {

	private HashMap<Long, SocketAddress> callServerAddresses;

	private HashMap<SocketAddress, ServerCalls> serverCalls;

	private static class ServerCalls {
		private SocketAddress serverAddress;
		private HashMap<Long, ClientCall> calls;

		ServerCalls(SocketAddress serverAddress) {
			this.serverAddress = serverAddress;
			calls = new HashMap<Long, ClientCall>();
		}

		public void remove(long callId) {
			this.calls.remove(callId);
		}

		public boolean isEmpty() {
			return this.calls.isEmpty();
		}

		public void addCall(ClientCall call) {
			this.calls.put(call.getId(), call);

		}

	}

	private TransportClient client;

	TransportConnector(TransportClient client) {
		this.client = client;
		callServerAddresses = new HashMap<Long, SocketAddress>();
		serverCalls = new HashMap<SocketAddress, TransportConnector.ServerCalls>();
	}

	public synchronized void sumbitCall(SocketAddress serverAddress,
			ClientCall call) throws ConnectException, IOException {
		ServerCalls serverCalls = this.serverCalls.get(serverAddress);
		if (serverCalls == null) {
			serverCalls = new ServerCalls(serverAddress);
			this.serverCalls.put(serverAddress, serverCalls);
		}
		serverCalls.addCall(call);
		this.callServerAddresses.put(call.getId(), serverAddress);
		this.client.submitCall(serverAddress, call.getRequestPacket());
	}

	public synchronized void returnCall(Packet responsePacket) {
		// TODO:
	}

	public synchronized void notifyServerError(SocketAddress serverAddress) {
		// TODO:
	}
}
