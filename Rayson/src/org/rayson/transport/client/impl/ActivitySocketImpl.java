package org.rayson.transport.client.impl;

import java.io.IOException;
import java.net.Socket;

import org.rayson.transport.client.ClientStreamConnection;
import org.rayson.transport.stream.AbstractActivitySocekt;

public class ActivitySocketImpl extends AbstractActivitySocekt {
	private ClientStreamConnection connection;

	public ActivitySocketImpl(ClientStreamConnection connection, Socket socket,
			short activity, short version) throws IOException {
		super(socket, activity, version);
		this.connection = connection;
	}

	@Override
	public void close() throws IOException {
		try {
			super.close();
		} finally {
			// close connection.
			this.connection.close();
		}

	}
}
