package org.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import org.rayson.api.ActivitySocket;
import org.rayson.transport.api.TimeLimitConnection;
import org.rayson.transport.client.impl.ActivitySocketImpl;
import org.rayson.transport.common.ConnectionProtocol;
import org.rayson.transport.common.ConnectionState;
import org.rayson.transport.common.ProtocolType;
import org.rayson.util.Log;

public class StreamConnection extends TimeLimitConnection {
	private long id;
	private SocketAddress serverAddress;
	private static final short version = 1;
	private static final long TIME_OUT_INTERVAL = 60 * 1000;
	private short activity;
	private SocketChannel socketChannel;
	private ByteBuffer connectHeaderBuffer;
	private ByteBuffer connectResponseBuffer;
	private ConnectionManager connectionManager;
	private static Logger LOGGER = Log.getLogger();

	public StreamConnection(SocketAddress serverAddress, short activity,
			ConnectionManager connectionManager) {
		this.id = ConnectionManager.getNextConnectionId();
		this.connectionManager = connectionManager;
		this.serverAddress = serverAddress;
		this.activity = activity;
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);

		this.connectHeaderBuffer.put(getProtocol().getType());
		this.connectHeaderBuffer.putShort(version);
		this.connectHeaderBuffer.clear();
	}

	public void init() throws IOException {
		// do connect to remote server.
		SocketChannel socketChannel = SocketChannel.open(this.serverAddress);
		this.socketChannel = socketChannel;
		// write connection header to remote server in blocked mode
		this.socketChannel.write(connectHeaderBuffer);
		// read response in blocked mode.
		this.socketChannel.read(connectResponseBuffer);
		connectResponseBuffer.flip();
		ConnectionState state = ConnectionState.valueOf(connectResponseBuffer
				.get());
		if (state != ConnectionState.OK)
			throw new ConnectException(state.name());
		socketChannel.configureBlocking(false);
		LOGGER.info(this.toString() + " builded");
	}

	@Override
	public void close() throws IOException {
		this.socketChannel.close();
	}

	void remove() {
		this.connectionManager.remove(this);
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

	public short getVersion() {
		return version;
	}

	@Override
	public void write() throws IOException {
		// TODO Auto-generated method stub

	}

	public short getActivity() {
		return activity;
	}

	@Override
	protected long getTimeoutInterval() {
		return TIME_OUT_INTERVAL;
	}

	ActivitySocket createActivitySocket() throws IOException {
		return new ActivitySocketImpl(this, socketChannel);
	}

}
