package org.rayson.transport.client;

import java.io.IOException;
import java.net.Socket;

import org.rayson.transport.stream.AbstractActivitySocekt;

class ActivitySocketImpl extends AbstractActivitySocekt {
	private ClientStreamConnection connection;

	public ActivitySocketImpl(ClientStreamConnection connection, Socket socket,
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
