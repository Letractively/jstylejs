package server;

import java.util.concurrent.ConcurrentHashMap;

class ConnectionManager {

	private ConcurrentHashMap<Long, ServerConnection> connections;

	ConnectionManager() {
		connections = new ConcurrentHashMap<Long, ServerConnection>();
	}

	public void accept(ServerConnection connection) {
		this.connections.put(connection.getId(), connection);
	}

	public void remove(ServerConnection connection) {
		this.connections.remove(connection.getId());
		System.out.println(connection.toString() + " removed!");
	}

	public int size() {
		return this.connections.size();
	}

}
