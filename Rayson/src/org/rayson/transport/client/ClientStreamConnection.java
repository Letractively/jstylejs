package org.rayson.transport.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.rayson.annotation.TransferCode;
import org.rayson.api.TransferArgument;
import org.rayson.api.TransferSocket;
import org.rayson.common.Stream;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.transport.api.TimeLimitConnection;
import org.rayson.transport.common.ConnectionProtocol;
import org.rayson.transport.common.ConnectionState;
import org.rayson.transport.common.ProtocolType;
import org.rayson.transport.stream.TransferResponse;
import org.rayson.util.Log;

class ClientStreamConnection extends TimeLimitConnection {
	private long id;
	private SocketAddress serverAddress;
	private static final short version = 1;
	private static final long TIME_OUT_INTERVAL = 60 * 1000;
	private static final int BUFFER_SIZE = 1024;
	private short transfer;
	private SocketChannel socketChannel;
	private ByteBuffer connectHeaderBuffer;
	private ByteBuffer connectResponseBuffer;
	private AtomicBoolean closed;
	private ConnectionManager connectionManager;
	private TransferArgument argument;
	private static Logger LOGGER = Log.getLogger();

	public ClientStreamConnection(SocketAddress serverAddress,
			TransferArgument argument, ConnectionManager connectionManager) {
		this.id = ConnectionManager.getNextConnectionId();
		this.connectionManager = connectionManager;
		this.serverAddress = serverAddress;
		TransferCode transferCode = argument.getClass().getAnnotation(
				TransferCode.class);
		this.transfer = transferCode.value();
		this.argument = argument;
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);

		this.connectHeaderBuffer.put(getProtocol().getType());
		this.connectHeaderBuffer.putShort(version);
		this.connectHeaderBuffer.clear();
		closed = new AtomicBoolean(false);
	}

	public void init() throws IOException, ConnectException,
			ServiceNotFoundException {
		// do connect to remote server.
		SocketChannel socketChannel = SocketChannel.open(this.serverAddress);
		this.socketChannel = socketChannel;
		try {
			// write connection header to remote server in blocked mode
			this.socketChannel.write(connectHeaderBuffer);
			// read response in blocked mode.
			this.socketChannel.read(connectResponseBuffer);
			connectResponseBuffer.flip();
			ConnectionState state = ConnectionState
					.valueOf(connectResponseBuffer.get());
			if (state != ConnectionState.OK)
				throw new ConnectException(state.name());
			socketChannel.configureBlocking(true);
			// send transfer number to remote
			connectHeaderBuffer.clear();
			connectHeaderBuffer.putShort(transfer);
			connectHeaderBuffer.flip();
			this.socketChannel.write(connectHeaderBuffer);

			// then write argument to remote server.
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					BUFFER_SIZE);
			DataOutputStream dataOutputStream = new DataOutputStream(
					byteArrayOutputStream);
			Stream.write(dataOutputStream, argument);
			byte[] argumentData = byteArrayOutputStream.toByteArray();
			ByteBuffer argumentBuffer = ByteBuffer
					.allocate(2 + argumentData.length);
			argumentBuffer.putShort((short) argumentData.length);
			argumentBuffer.put(argumentData);
			argumentBuffer.flip();
			socketChannel.write(argumentBuffer);
			// read response from remote
			connectResponseBuffer.clear();
			this.socketChannel.read(connectResponseBuffer);
			connectResponseBuffer.flip();
			TransferResponse response = TransferResponse
					.valueOf(connectResponseBuffer.get());
			if (response != TransferResponse.OK)
				throw new ServiceNotFoundException("No transfer " + transfer
						+ " service found in servder:" + response.name());

		} catch (IOException e) {
			this.socketChannel.close();
			throw e;
		} catch (ServiceNotFoundException e) {
			this.socketChannel.close();
			throw e;
		}
		LOGGER.info(this.toString() + " builded");
	}

	@Override
	public void close() throws IOException {
		if (closed.compareAndSet(false, true)) {
			this.socketChannel.close();
		}
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
		// TODO:
		return 0;
	}

	public short getVersion() {
		return version;
	}

	@Override
	public void write() throws IOException {
		// TODO:
		return;
	}

	public short getTransfer() {
		return transfer;
	}

	@Override
	protected long getTimeoutInterval() {
		return TIME_OUT_INTERVAL;
	}

	TransferSocket createTransferSocket() throws IOException {
		return new TransferSocketImpl(this, socketChannel.socket(), transfer,
				version);
	}

}
