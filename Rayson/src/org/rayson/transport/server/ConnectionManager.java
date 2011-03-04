package org.rayson.transport.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rayson.transport.common.AbstractConnection;
import org.rayson.transport.common.Connection;
import org.rayson.transport.common.ConnectionProtocol;
import org.rayson.util.Log;

class ConnectionManager extends Thread {

	private static Logger LOGGER = Log.getLogger();
	private static final int THECK_TIME_OUT_INTERVAL = ConnectionProtocol.TIME_OUT_INTERVAL / 2;

	private ConcurrentHashMap<Long, AbstractConnection> connections;
	private static final int MAX_PENDINGS = 10000;
	private HashMap<Long, PendingConnection> pendings;

	ConnectionManager() {
		setName("Connection manager");
		connections = new ConcurrentHashMap<Long, AbstractConnection>();
		pendings = new HashMap<Long, PendingConnection>();
	}

	public void accept(long pendingId, RpcConnection connection) {
		// throw new DenyServiceException();
		this.pendings.remove(pendingId);
		this.connections.put(connection.getId(), connection);
	}

	private void checkTimeouts() {
		for (Iterator<AbstractConnection> iterator = this.connections.values()
				.iterator(); iterator.hasNext();) {
			AbstractConnection conn = iterator.next();
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

	public void remove(Connection connection) {
		if (connection instanceof PendingConnection)
			this.pendings.remove(connection.getId());
		else
			this.connections.remove(connection.getId());
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

	public void acceptPending(PendingConnection connection)
			throws DenyServiceException {
		if (this.pendings.size() > MAX_PENDINGS)
			throw new DenyServiceException();
		this.pendings.put(connection.getId(), connection);
	}
}