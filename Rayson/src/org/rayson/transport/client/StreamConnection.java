package org.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;

import org.rayson.transport.api.Connection;
import org.rayson.transport.api.TimeLimitConnection;
import org.rayson.transport.common.ProtocolType;

class StreamConnection extends TimeLimitConnection {
	private long id;
	private SocketAddress serverAddress;
	private short activity;

	public StreamConnection(SocketAddress serverAddress, short activity,
			PacketManager packetManager) {
		this.id = ConnectionManager.getNextConnectionId();
		this.serverAddress = serverAddress;
		this.activity = activity;
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

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

	@Override
	protected long getTimeoutInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

}
