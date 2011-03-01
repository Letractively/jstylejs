package org.rayson.transport.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;

import org.rayson.client.ClientCall;
import org.rayson.transport.common.Packet;
import org.rayson.util.Log;

public class RpcConnector {

	private static class CallWrapper {
		private ClientCall call;
		private long connectionId;

		CallWrapper(ClientCall call, long connectionId) {
			this.call = call;
			this.connectionId = connectionId;
		}

		public ClientCall getCall() {
			return call;
		}

		public long getConnectionId() {
			return connectionId;
		}
	}

	private class ConnectionCalls {
		private HashSet<Long> calls;
		private long connectionId;

		ConnectionCalls(long connectionId) {
			this.connectionId = connectionId;
			this.calls = new HashSet<Long>();
		}

		public void addCall(long callId) {
			this.calls.add(callId);
		}

		public void removeCall(long callId) {
			this.calls.remove(callId);
		}

		public Iterator<Long> iterator() {
			return this.calls.iterator();
		}
	}

	private HashMap<Long, CallWrapper> calls;
	private HashMap<Long, ConnectionCalls> connections;
	private TransportClient client;

	RpcConnector(TransportClient client) {
		this.client = client;
		calls = new HashMap<Long, CallWrapper>();
		connections = new HashMap<Long, RpcConnector.ConnectionCalls>();
	}

	public synchronized void notifyConnectionClosed(ClientConnection connection) {
		ConnectionCalls connectionCalls = connections
				.remove(connection.getId());
		if (connectionCalls == null) {
			Log.getLogger().log(
					Level.SEVERE,
					"Can not find connection calls in connection :"
							+ connection.getId());
			return;
		}

		// remove from call list.
		for (Iterator<Long> iterator = connectionCalls.iterator(); iterator
				.hasNext();) {
			Long callId = iterator.next();
			CallWrapper callWrapper = this.calls.remove(callId);
			if (callWrapper == null)
				continue;
			callWrapper.call.notifyConnectionClosed();
		}
	}

	private synchronized void addCall(ClientCall call, long connectionId) {
		this.calls.put(call.getId(), new CallWrapper(call, connectionId));
		ConnectionCalls connectionCalls = connections.get(connectionId);
		if (connectionCalls == null) {
			connectionCalls = new ConnectionCalls(connectionId);
			connections.put(connectionId, connectionCalls);
		}
		connectionCalls.addCall(call.getId());
	}

	private synchronized ClientCall removeCall(long callId) {
		CallWrapper callWrapper = calls.remove(callId);
		if (callWrapper == null) {
			return null;
		}
		// remove from connection calls
		ConnectionCalls connectionCalls = connections
				.get(callWrapper.connectionId);
		if (connectionCalls == null) {
			Log.getLogger().log(
					Level.SEVERE,
					"Can not find call in connection :"
							+ callWrapper.connectionId);
			return null;
		}
		connectionCalls.removeCall(callId);
		return callWrapper.call;
	}

	public synchronized void sumbitCall(SocketAddress serverAddress,
			ClientCall call) throws ConnectException, IOException {
		ClientConnection connection = client.getConnection(serverAddress);
		addCall(call, connection.getId());
		connection.addSendPacket(call.getRequestPacket());

	}

	public void responseOneCall() throws InterruptedException {
		Packet receivedPacket = this.client.getPacketManager().takeReceived();
		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(receivedPacket.getData()));
		long callId = -1;
		try {
			callId = inputStream.readLong();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClientCall call = removeCall(callId);
		if (call == null) {
			return;
		}
		call.readResult(inputStream);
	}
}
