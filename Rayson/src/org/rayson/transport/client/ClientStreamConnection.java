package org.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.rayson.api.ActivitySocket;
import org.rayson.transport.api.StreamConnection;
import org.rayson.transport.api.TimeLimitConnection;
import org.rayson.transport.common.ConnectionProtocol;
import org.rayson.transport.common.ConnectionState;
import org.rayson.transport.common.ProtocolType;
import org.rayson.transport.stream.ActivitySocketImpl;
import org.rayson.transport.stream.StreamInputBuffer;
import org.rayson.transport.stream.StreamOutputBuffer;
import org.rayson.util.Log;

public class ClientStreamConnection extends TimeLimitConnection implements
		StreamConnection {
	private long id;

	private StreamInputBuffer inputBuffer;
	private StreamOutputBuffer outputBuffer;
	private static final int STREAM_BUFFER_SIZE = 1024 * 60;

	private SocketAddress serverAddress;
	private static final short version = 1;
	private static final long TIME_OUT_INTERVAL = 60 * 1000;
	private short activity;
	private SocketChannel socketChannel;
	private ByteBuffer connectHeaderBuffer;
	private ByteBuffer connectResponseBuffer;
	private AtomicBoolean closed;
	private Listener listener;

	private SelectionKey selectionKey;
	private static Logger LOGGER = Log.getLogger();

	public ClientStreamConnection(SocketAddress serverAddress, short activity,
			Listener listener) {
		this.id = ConnectionManager.getNextConnectionId();
		this.listener = listener;
		this.serverAddress = serverAddress;
		this.activity = activity;
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);

		this.connectHeaderBuffer.put(getProtocol().getType());
		this.connectHeaderBuffer.putShort(version);
		this.connectHeaderBuffer.clear();
		closed = new AtomicBoolean(false);
	}

	public void init() throws IOException, ConnectException {
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
		this.selectionKey = listener.register(this.socketChannel,
				SelectionKey.OP_READ, this);
		this.inputBuffer = new StreamInputBuffer(socketChannel, STREAM_BUFFER_SIZE);
		this.outputBuffer = new StreamOutputBuffer(socketChannel, selectionKey,
				STREAM_BUFFER_SIZE);
		LOGGER.info(this.toString() + " builded");
	}

	@Override
	public void close() throws IOException {
		if (closed.compareAndSet(false, true))
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
	public StreamInputBuffer getInputBuffer() {
		return inputBuffer;
	}

	@Override
	public StreamOutputBuffer getOutputBuffer() {
		return outputBuffer;
	}

	@Override
	public int read() throws IOException {
		return this.inputBuffer.asyncReadChannel();
	}

	public short getVersion() {
		return version;
	}

	@Override
	public void write() throws IOException {
		this.outputBuffer.asyncWriteChannel();
	}

	public short getActivity() {
		return activity;
	}

	@Override
	protected long getTimeoutInterval() {
		return TIME_OUT_INTERVAL;
	}

	ActivitySocket createActivitySocket() throws IOException {
		return new ActivitySocketImpl(this);
	}

}
