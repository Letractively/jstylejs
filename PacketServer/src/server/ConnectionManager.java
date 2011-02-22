package server;

import java.util.concurrent.ConcurrentHashMap;

class ConnectionManager {

	private ConcurrentHashMap<Long, ServerConnection> connections;

	ConnectionManager() {
		connections = new ConcurrentHashMap<Long, ServerConnection>();
	}

	public void accept(ServerConnection connection) throws DenyServiceException {
		// throw new DenyServiceException();
		this.connections.put(connection.getId(), connection);
	}

	public void remove(ServerConnection connection) {
		this.connections.remove(connection.getId());
	}

	public int size() {
		return this.connections.size();
	}

}