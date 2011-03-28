package org.rayson.transport.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;

import org.rayson.api.TransferArgument;
import org.rayson.api.TransferSocket;
import org.rayson.client.ClientCall;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.NetWorkException;
import org.rayson.exception.ServiceNotFoundException;
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

		public Iterator<Long> iterator() {
			return this.calls.iterator();
		}

		public void removeCall(long callId) {
			this.calls.remove(callId);
		}
	}

	private HashMap<Long, CallWrapper> calls;
	private TransportClient client;
	private HashMap<Long, ConnectionCalls> connections;

	RpcConnector(TransportClient client) {
		this.client = client;
		calls = new HashMap<Long, CallWrapper>();
		connections = new HashMap<Long, RpcConnector.ConnectionCalls>();
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

	public synchronized void notifyConnectionClosed(RpcConnection connection) {
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

	public void ping(SocketAddress serverAddress) throws NetWorkException {
		client.ping(serverAddress);
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

	public synchronized void sumbitCall(SocketAddress serverAddress,
			ClientCall call) throws ConnectException, IOException {
		RpcConnection connection = client.getConnection(serverAddress);
		addCall(call, connection.getId());
		connection.addSendPacket(call.getRequestPacket());

	}

	public TransferSocket openTransferSocket(SocketAddress serverAddress,
			TransferArgument argument) throws ConnectException, IOException,
			ServiceNotFoundException, IllegalServiceException {
		return client.createTransferSocket(serverAddress, argument);
	}
}
