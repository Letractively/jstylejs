package org.rayson.transport.server;

import java.io.IOException;
import java.net.Socket;

import org.rayson.transport.stream.AbstractActivitySocekt;

class ActivitySocketImpl extends AbstractActivitySocekt {
	private ServerStreamConnection connection;

	public ActivitySocketImpl(ServerStreamConnection connection, Socket socket,
			short activity, short version) throws IOException {
		super(socket, activity, version);
		this.connection = connection;
	}

	@Override
	public void close() throws IOException {
		try {
			// close connection.
			this.connection.close();
			super.close();
		} finally {
			// remove the connection
			this.connection.remove();
		}
	}
}
