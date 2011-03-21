package org.rayson.transport.client;

import java.io.IOException;
import java.net.Socket;

import org.rayson.transport.stream.AbstractTransferSocekt;

class TransferSocketImpl extends AbstractTransferSocekt {
	private ClientStreamConnection connection;

	public TransferSocketImpl(ClientStreamConnection connection, Socket socket,
			short transfer, short version) throws IOException {
		super(socket, transfer, version);
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
