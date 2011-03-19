package org.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rayson.transport.common.ConnectionProtocol;
import org.rayson.util.Log;

class ConnectionManager extends Thread {
	private static Logger LOGGER = Log.getLogger();
	private static final int THECK_TIME_OUT_INTERVAL = ConnectionProtocol.TIME_OUT_INTERVAL * 2;
	private static AtomicLong CONNECTION_UID = new AtomicLong(0);

	public static long getNextConnectionId() {
		return CONNECTION_UID.getAndIncrement();
	}

	private ConcurrentHashMap<SocketAddress, RpcConnection> rpcConnections;
	private ConcurrentHashMap<Long, StreamConnection> streamConnections;

	ConnectionManager() {
		setName("Client connection manager");
		rpcConnections = new ConcurrentHashMap<SocketAddress, RpcConnection>();
		streamConnections = new ConcurrentHashMap<Long, StreamConnection>();
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
					iterator.remove();
					conn.close();
				} catch (IOException e) {
					LOGGER.log(
							Level.SEVERE,
							"Close time out connection error: "
									+ e.getMessage());
				}

		}
		for (Iterator<StreamConnection> iterator = this.streamConnections
				.values().iterator(); iterator.hasNext();) {
			StreamConnection conn = iterator.next();
			if (conn.isTimeOut())
				try {
					LOGGER.info("Remove and close time out conection: "
							+ conn.toString());
					iterator.remove();
					conn.close();
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
	}

	public void remove(StreamConnection connection) {
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

	public void accept(StreamConnection connection) {
		this.streamConnections.put(connection.getId(), connection);
	}
}
