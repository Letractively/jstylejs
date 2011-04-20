/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.creativor.rayson.transport.common.ConnectionProtocol;
import org.creativor.rayson.util.Log;

/**
 *
 * @author Nick Zhang
 */
class ConnectionManager extends Thread {
	private static Logger LOGGER = Log.getLogger();
	private static final int THECK_TIME_OUT_INTERVAL = ConnectionProtocol.TIME_OUT_INTERVAL * 2;
	private static AtomicLong CONNECTION_UID = new AtomicLong(0);

	public static long getNextConnectionId() {
		return CONNECTION_UID.getAndIncrement();
	}

	private ConcurrentHashMap<SocketAddress, RpcConnection> rpcConnections;
	private ConcurrentHashMap<Long, ClientStreamConnection> streamConnections;

	ConnectionManager() {
		setName("Client connection manager");
		rpcConnections = new ConcurrentHashMap<SocketAddress, RpcConnection>();
		streamConnections = new ConcurrentHashMap<Long, ClientStreamConnection>();
	}

	public void accept(RpcConnection connection) {
		// throw new DenyServiceException();
		this.rpcConnections.put(connection.getServerSocket(), connection);
	}

	private void checkTimeouts() {
		for (Iterator<RpcConnection> iterator = this.rpcConnections.values()
				.iterator(); iterator.hasNext();) {
			RpcConnection conn = iterator.next();
			if (conn.isTimeOut())
				try {
					LOGGER.info("Remove and close time out conection: "
							+ conn.toString());
					conn.close();
					this.remove(conn);
				} catch (IOException e) {
					LOGGER.log(
							Level.SEVERE,
							"Close time out connection error: "
									+ e.getMessage());
				}

		}
		for (Iterator<ClientStreamConnection> iterator = this.streamConnections
				.values().iterator(); iterator.hasNext();) {
			ClientStreamConnection conn = iterator.next();
			if (conn.isTimeOut())
				try {
					LOGGER.info("Remove and close time out conection: "
							+ conn.toString());
					conn.close();
					this.remove(conn);
				} catch (IOException e) {
					LOGGER.log(
							Level.SEVERE,
							"Close time out connection error: "
									+ e.getMessage());
				}

		}
	}

	public RpcConnection getConnection(SocketAddress serverAddress) {
		return this.rpcConnections.get(serverAddress);

	}

	public void remove(RpcConnection connection) {
		this.rpcConnections.remove(connection.getServerSocket());
		connection.notifyRemoved();
	}

	public void remove(ClientStreamConnection connection) {
		this.streamConnections.remove(connection.getId());
	}

	@Override
	public void run() {
		LOGGER.info(getName() + " starting...");
		while (true) {
			try {
				Thread.sleep(THECK_TIME_OUT_INTERVAL);
			} catch (InterruptedException e) {
				break;
			}
			checkTimeouts();
		}

		LOGGER.info(getName() + " stoped");

	}

	public int size() {
		return this.rpcConnections.size();
	}

	public void accept(ClientStreamConnection connection) {
		this.streamConnections.put(connection.getId(), connection);
	}
}
