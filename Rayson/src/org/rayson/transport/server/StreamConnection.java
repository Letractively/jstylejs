package org.rayson.transport.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.rayson.transport.api.Connection;
import org.rayson.transport.api.TimeLimitConnection;
import org.rayson.transport.common.ProtocolType;

class StreamConnection extends TimeLimitConnection {
	private static final int TIME_OUT_INTERVAL = 30000;
	private long id;
	private SocketChannel socketChannel;
	private SelectionKey selectionKey;

	public StreamConnection(long id, SocketChannel socketChannel,
			SelectionKey selectionKey) {
		this.id = id;
		this.socketChannel = socketChannel;
		this.selectionKey = selectionKey;
	}

	@Override
	protected long getTimeoutInterval() {
		return TIME_OUT_INTERVAL;
	}

	@Override
	public void close() throws IOException {
		this.socketChannel.close();
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public ProtocolType getProtocol() {
		return ProtocolType.STREAM;
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void write() throws IOException {
		// TODO Auto-generated method stub

	}

}