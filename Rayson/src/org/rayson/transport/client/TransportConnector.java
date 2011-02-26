package org.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import org.rayson.client.ClientCall;

public class TransportConnector {

	private ConcurrentHashMap<Long, SocketAddress> callServerAddresses;

	private TransportClient client;

	TransportConnector(TransportClient client) {
		this.client = client;
		callServerAddresses = new ConcurrentHashMap<Long, SocketAddress>();
	}

	public void sumbitCall(SocketAddress serverAddress, ClientCall call)
			throws ConnectException, IOException {
		this.callServerAddresses.put(call.getId(), serverAddress);
		this.client.submitCall(serverAddress, call.getRequestPacket());
	}
}
