package org.creativor.rayson.transport.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.creativor.rayson.annotation.ClientVersion;
import org.creativor.rayson.annotation.TransferCode;
import org.creativor.rayson.api.TransferArgument;
import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.client.Rayson;
import org.creativor.rayson.common.Stream;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.transport.api.TimeLimitConnection;
import org.creativor.rayson.transport.common.ConnectionProtocol;
import org.creativor.rayson.transport.common.ConnectionState;
import org.creativor.rayson.transport.common.ProtocolType;
import org.creativor.rayson.transport.stream.TransferResponse;
import org.creativor.rayson.util.Log;

class ClientStreamConnection extends TimeLimitConnection {
	private long id;
	private SocketAddress serverAddress;
	private static final byte version = Rayson.getClientVersion();
	private static final long TIME_OUT_INTERVAL = 60 * 1000;
	private static final int BUFFER_SIZE = 1024;
	private short transferCode;
	private short clientVersion;
	private SocketChannel socketChannel;
	private ByteBuffer connectHeaderBuffer;
	private ByteBuffer connectResponseBuffer;
	private ByteBuffer transferResponseBuffer;
	private AtomicBoolean closed;
	private ConnectionManager connectionManager;
	private TransferArgument argument;
	private static Logger LOGGER = Log.getLogger();

	public ClientStreamConnection(SocketAddress serverAddress,
			TransferArgument argument, ConnectionManager connectionManager)
			throws IllegalServiceException {
		this.id = ConnectionManager.getNextConnectionId();
		this.connectionManager = connectionManager;
		this.serverAddress = serverAddress;
		ClientVersion clientVersionAnnotation = argument.getClass()
				.getAnnotation(ClientVersion.class);
		if (clientVersionAnnotation == null)
			clientVersion = ClientVersion.DEFAULT_VALUE;
		else
			clientVersion = clientVersionAnnotation.value();
		TransferCode transferCode = argument.getClass().getAnnotation(
				TransferCode.class);
		// Verify transfer code.
		if (transferCode == null)
			throw new IllegalServiceException(
					"No transfer code annotation found in argument class");
		this.transferCode = transferCode.value();
		this.argument = argument;
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);
		transferResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.TRANSFER_RESPONSE_LENGTH);
		this.connectHeaderBuffer.put(getProtocol().getType());
		this.connectHeaderBuffer.put(version);
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
				throw new ConnectException("Get wrong connection state: "
						+ state.name());
			socketChannel.configureBlocking(true);
			connectHeaderBuffer.clear();
			// send client version to remote.
			connectHeaderBuffer.putShort(clientVersion);
			// send transfer number to remote
			connectHeaderBuffer.putShort(transferCode);
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
			this.socketChannel.read(transferResponseBuffer);
			transferResponseBuffer.flip();
			TransferResponse response = TransferResponse
					.valueOf(transferResponseBuffer.get());
			if (response == TransferResponse.NO_ACTIVITY_FOUND)
				throw new ServiceNotFoundException("No transfer "
						+ transferCode + " service found in servder:"
						+ response.name());
			// if(response==TransferResponse.UNSUPPORTED_VERSION) throw new unsu

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

	public byte getVersion() {
		return version;
	}

	@Override
	public void write() throws IOException {
		// TODO:
		return;
	}

	public short getTransfer() {
		return transferCode;
	}

	@Override
	protected long getTimeoutInterval() {
		return TIME_OUT_INTERVAL;
	}

	TransferSocket createTransferSocket() throws IOException {
		return new TransferSocketImpl(this, socketChannel.socket(),
				transferCode, version);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id: ");
		sb.append(this.id);
		sb.append(", protocol: ");
		sb.append(this.getProtocol());
		sb.append(", version: ");
		sb.append(this.getVersion());
		sb.append(", last contact: ");
		sb.append(getLastContactTime());
		sb.append(", address: ");
		sb.append(this.socketChannel.socket().toString());
		sb.append("}");
		return sb.toString();
	}
}