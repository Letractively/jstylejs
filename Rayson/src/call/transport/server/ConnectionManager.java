package call.transport.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import call.transport.common.ConnectionProtocol;



class ConnectionManager extends Thread {

	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final int THECK_TIME_OUT_INTERVAL = ConnectionProtocol.TIME_OUT_INTERVAL / 2;

	private ConcurrentHashMap<Long, ServerConnection> connections;

	ConnectionManager() {
		setName("Server connection manager");
		connections = new ConcurrentHashMap<Long, ServerConnection>();
	}

	public void accept(ServerConnection connection) throws DenyServiceException {
		// throw new DenyServiceException();
		this.connections.put(connection.getId(), connection);
	}

	private void checkTimeouts() {
		for (Iterator<ServerConnection> iterator = this.connections.values()
				.iterator(); iterator.hasNext();) {
			ServerConnection conn = iterator.next();
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

	public void remove(ServerConnection connection) {
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
}