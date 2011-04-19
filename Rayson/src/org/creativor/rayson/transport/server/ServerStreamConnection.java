package org.creativor.rayson.transport.server;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import org.creativor.rayson.api.TransferArgument;
import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.common.Stream;
import org.creativor.rayson.transport.api.TimeLimitConnection;
import org.creativor.rayson.transport.common.ConnectionProtocol;
import org.creativor.rayson.transport.common.ConnectionState;
import org.creativor.rayson.transport.common.ProtocolType;
import org.creativor.rayson.transport.server.transfer.TransferCallException;
import org.creativor.rayson.transport.server.transfer.TransferConnector;
import org.creativor.rayson.transport.stream.TransferResponse;
import org.creativor.rayson.util.Log;

class ServerStreamConnection extends TimeLimitConnection {

	private static enum ReadState {
		ARGUMENT_DATA, DATA_SIZE, TRANSFER_CODE, VERSION;
	}

	private static Logger LOGGER = Log.getLogger();
	private static final int TIME_OUT_INTERVAL = 30000;
	private TransferArgument argument;
	private ByteBuffer argumentBuffer;
	private short code;
	private ByteBuffer connectHeaderBuffer;
	private ConnectionManager connectionManager;
	private ConnectionState connectionState;
	private ByteBuffer connectResponseBuffer;
	private long id;
	private ReadState readState;
	private SelectionKey selectionKey;
	private SocketChannel socketChannel;
	private ByteBuffer transferCodeBuffer;
	private TransferConnector transferConnector;
	private TransferResponse transferResponse;
	private ByteBuffer transferResponseBuffer;

	private short clientVersion = -1;

	private boolean wroteConnectCode = false;
	private byte version = -1;

	public ServerStreamConnection(long id, SocketChannel socketChannel,
			SelectionKey selectionKey, ConnectionManager connectionManager,
			TransferConnector transferConnector) {
		this.id = id;
		this.socketChannel = socketChannel;
		this.connectionManager = connectionManager;
		this.transferConnector = transferConnector;
		this.selectionKey = selectionKey;
		readState = ReadState.VERSION;
		transferCodeBuffer = ByteBuffer.allocate(2);
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH - 1);// header length
		// -protocol
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);
		transferResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.TRANSFER_RESPONSE_LENGTH);
		setConnectionState(ConnectionState.OK);
	}

	@Override
	public void close() throws IOException {
		this.socketChannel.close();
	}

	public short getCode() {
		return this.code;
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
	protected long getTimeoutInterval() {
		return TIME_OUT_INTERVAL;
	}

	@Override
	public byte getVersion() {
		return version;
	}

	boolean isSupportedVersion(short version) {
		if (version < -1 || version > 3)
			return false;
		return true;
	}

	@Override
	public int read() throws IOException {
		int readCount = -1;
		switch (readState) {
		case VERSION:
			readCount = this.socketChannel.read(connectHeaderBuffer);
			if (!connectHeaderBuffer.hasRemaining()) {
				connectHeaderBuffer.flip();
				clientVersion = connectHeaderBuffer.get();
				if (!isSupportedVersion(clientVersion))
					setConnectionState(ConnectionState.UNSUPPORTED_VERSION);
				this.selectionKey.interestOps(SelectionKey.OP_WRITE
						| SelectionKey.OP_READ);
				readState = ReadState.TRANSFER_CODE;
			}
			break;
		case TRANSFER_CODE:
			readCount = this.socketChannel.read(transferCodeBuffer);
			if (!this.transferCodeBuffer.hasRemaining()) {
				this.transferCodeBuffer.flip();
				this.code = this.transferCodeBuffer.getShort();
				boolean serviceExists = this.transferConnector
						.serviceExists(this.code);
				// set transfer response code
				if (serviceExists)

					setTransferResponse(TransferResponse.OK);
				else
					setTransferResponse(TransferResponse.NO_ACTIVITY_FOUND);
				transferCodeBuffer.clear();
				this.readState = ReadState.DATA_SIZE;
			}
			break;

		case DATA_SIZE:
			readCount = this.socketChannel.read(transferCodeBuffer);
			if (!this.transferCodeBuffer.hasRemaining()) {
				this.transferCodeBuffer.flip();
				int dataLength = this.transferCodeBuffer.getShort();
				this.argumentBuffer = ByteBuffer.allocate(dataLength);
				this.readState = ReadState.ARGUMENT_DATA;
			}
			break;
		case ARGUMENT_DATA:

			readCount = this.socketChannel.read(argumentBuffer);
			if (!this.argumentBuffer.hasRemaining()) {
				this.argumentBuffer.flip();
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						this.argumentBuffer.array());
				DataInputStream dataInputStream = new DataInputStream(
						byteArrayInputStream);
				try {
					this.argument = (TransferArgument) Stream
							.read(dataInputStream);
				} catch (IOException e) {
					throw e;
				}
				this.selectionKey.interestOps(SelectionKey.OP_WRITE
						| SelectionKey.OP_READ);
			}

			break;
		default:
			break;
		}

		return readCount;
	}

	public void remove() {
		this.connectionManager.remove(this);
	}

	private void setConnectionState(ConnectionState connectionCode) {
		this.connectionState = connectionCode;
		this.connectResponseBuffer.clear();
		this.connectResponseBuffer.put(connectionCode.getState());
		this.connectResponseBuffer.clear();
	}

	private void setTransferResponse(TransferResponse response) {
		this.transferResponse = response;
		this.transferResponseBuffer.clear();
		this.transferResponseBuffer.put(response.getCode());
		this.transferResponseBuffer.clear();
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
		sb.append(", code: ");
		sb.append(this.code);
		sb.append("}");
		return sb.toString();
	}

	@Override
	public void write() throws IOException {
		if (wroteConnectCode) {
			// write transfer response code.
			this.socketChannel.write(transferResponseBuffer);
			if (!this.transferResponseBuffer.hasRemaining()) {
				// try close this connection itself.
				switch (transferResponse) {
				case NO_ACTIVITY_FOUND:
				case UNKNOWN:
				case ARGUMENT_ERROR:
					try {
						this.close();
					} catch (IOException e) {
						// Ignore.
					}
					break;
				case OK:
					// remove selection key.
					this.selectionKey.cancel();
					// set socket channel to blocked mode.
					this.socketChannel.configureBlocking(true);
					// // add a new transferCall.
					TransferSocket transferSocket = new TransferSocketImpl(
							this, this.socketChannel.socket(), code,
							clientVersion);

					LOGGER.info("Transfer socket: " + transferSocket.toString()
							+ " build");
					try {
						this.transferConnector.submitCall(this.code, argument,
								transferSocket);
					} catch (TransferCallException e) {
						throw new IOException(e);
					}
				default:
					break;
				}

			}
		} else {
			// write connection response code.
			this.socketChannel.write(connectResponseBuffer);
			if (!this.connectResponseBuffer.hasRemaining()) {
				this.selectionKey.interestOps(SelectionKey.OP_READ);
				wroteConnectCode = true;
				// try close this connection itself.
				switch (connectionState) {
				case SERVICE_UNAVALIABLE:
					try {
						this.close();
					} catch (IOException e) {
						// Ignore.
					}
					break;
				case UNSUPPORTED_VERSION:
					throw new IOException("Wrong version");

				default:
					break;
				}

			}
		}
	}
}