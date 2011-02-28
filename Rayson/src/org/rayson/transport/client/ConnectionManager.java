package org.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rayson.transport.common.ConnectionProtocol;
import org.rayson.util.Log;

class ConnectionManager extends Thread {
	private static Logger LOGGER = Log.getLogger();
	private static final int THECK_TIME_OUT_INTERVAL = ConnectionProtocol.TIME_OUT_INTERVAL * 2;

	private ConcurrentHashMap<SocketAddress, ClientConnection> connections;

	ConnectionManager() {
		setName("Client connection manager");
		connections = new ConcurrentHashMap<SocketAddress, ClientConnection>();
	}

	public void accept(ClientConnection connection) {
		// throw new DenyServiceException();
		this.connections.put(connection.getServerSocket(), connection);
	}

	private void checkTimeouts() {
		for (Iterator<ClientConnection> iterator = this.connections.values()
				.iterator(); iterator.hasNext();) {
			ClientConnection conn = iterator.next();
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

	public ClientConnection getConnection(SocketAddress serverAddress) {
		return this.connections.get(serverAddress);

	}

	public void remove(ClientConnection connection) {
		this.connections.remove(connection.getServerSocket());
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
		return this.connections.size();
	}
}
