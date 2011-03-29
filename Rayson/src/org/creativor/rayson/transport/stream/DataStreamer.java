package org.creativor.rayson.transport.stream;

import java.io.IOException;

import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.transport.api.TimeLimitConnection;

abstract class DataStreamer {
	private TransferSocket transferSocket;
	private TimeLimitConnection connection;

	DataStreamer(TransferSocket transferSocket, TimeLimitConnection connection) {
		this.transferSocket = transferSocket;
		this.connection = connection;
	}

	protected void catchIOException(IOException e) throws IOException {
		try {
			this.transferSocket.close();
		} finally {

		}
		throw e;
	}

	protected void touch() {
		this.connection.touch();
	}
}
