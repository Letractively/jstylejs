/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.creativor.rayson.server.ServerCall;

/**
 * 
 * @author Nick Zhang
 */
public class RpcConnector {

	/**
	 * Map of <rpc call id, rpc connection>.
	 */
	private HashMap<Long, RpcConnection> callConenctions;
	private TransportServer server;
	private Lock lock;

	RpcConnector(TransportServer server) {
		this.server = server;
		callConenctions = new HashMap<Long, RpcConnection>();
		lock = new ReentrantLock();
	}

	public void responseCall(ServerCall call) throws IOException {
		RpcConnection serverConnection;
		lock.lock();
		try {
			serverConnection = this.callConenctions.remove(call.getId());
		} finally {
			lock.unlock();
		}
		serverConnection.addSendPacket(call.getResponsePacket());
	}

	public ServerCall takeCall() throws InterruptedException {
		ConnectionPacketLink connectionPacket = this.server.getPacketManager()
				.takeReceived();
		ServerCall serverCall = null;
		serverCall = ServerCall.fromPacket(connectionPacket.getConnection()
				.getRemoteAddr(), connectionPacket.getPacket());
		lock.lock();
		try {
			callConenctions.put(serverCall.getId(),
					connectionPacket.getConnection());
		} finally {
			lock.unlock();
		}
		return serverCall;
	}
}
