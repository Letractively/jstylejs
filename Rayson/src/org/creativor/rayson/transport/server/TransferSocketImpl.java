package org.creativor.rayson.transport.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import org.creativor.rayson.transport.stream.AbstractTransferSocket;

class TransferSocketImpl extends AbstractTransferSocket {
	private ServerStreamConnection connection;
	private AtomicBoolean closed;

	public TransferSocketImpl(ServerStreamConnection connection, Socket socket,
			short transfer, short version) throws IOException {
		super(connection, socket, transfer, version);
		this.connection = connection;
		closed = new AtomicBoolean(false);
	}

	@Override
	public void close() throws IOException {
		if (!closed.compareAndSet(false, true))
			return;
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
