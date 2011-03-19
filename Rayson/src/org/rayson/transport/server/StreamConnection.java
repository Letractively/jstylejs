package org.rayson.transport.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import org.rayson.transport.api.TimeLimitConnection;
import org.rayson.transport.common.ConnectionProtocol;
import org.rayson.transport.common.ConnectionState;
import org.rayson.transport.common.ProtocolType;
import org.rayson.util.Log;

class StreamConnection extends TimeLimitConnection {
	private static final int TIME_OUT_INTERVAL = 30000;
	private static Logger LOGGER = Log.getLogger();
	private ByteBuffer connectHeaderBuffer;
	private ByteBuffer connectResponseBuffer;

	private long id;
	private SocketChannel socketChannel;
	private boolean readedConnectHeader = false;

	private SelectionKey selectionKey;
	private short version = -1;
	private short activity;

	private ByteBuffer activityBuffer;
	private boolean readActivity = false;

	public StreamConnection(long id, SocketChannel socketChannel,
			SelectionKey selectionKey) {
		this.id = id;
		this.socketChannel = socketChannel;
		this.selectionKey = selectionKey;
		activityBuffer = ByteBuffer.allocate(2);
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH - 1);// header length
		// -protocol
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);

		setConnectionState(ConnectionState.OK);

	}

	@Override
	public short getVersion() {
		return version;
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
		if (readedConnectHeader) {
			if (!readActivity) {
				this.socketChannel.read(activityBuffer);
				if (!this.activityBuffer.hasRemaining()) {
					this.activityBuffer.flip();
					this.activity = this.activityBuffer.getShort();
					this.readActivity = true;
					return 2;
				}
			}
			// TODO:
			return 0;
		} else {
			init();
			return 0;
		}
	}

	public short getActivity() {
		return this.activity;
	}

	private ConnectionState connectionState;

	private void init() throws IOException {
		this.socketChannel.read(connectHeaderBuffer);
		if (!connectHeaderBuffer.hasRemaining()) {
			connectHeaderBuffer.flip();
			version = connectHeaderBuffer.getShort();
			if (!isSupportedVersion(version))
				setConnectionState(ConnectionState.UNSUPPORTED_VERSION);
			this.selectionKey.interestOps(SelectionKey.OP_WRITE
					| SelectionKey.OP_READ);
			readedConnectHeader = true;
		}
	}

	private void setConnectionState(ConnectionState connectionCode) {
		this.connectionState = connectionCode;
		this.connectResponseBuffer.clear();
		this.connectResponseBuffer.put(connectionCode.getState());
		this.connectResponseBuffer.clear();
	}

	boolean isSupportedVersion(short version) {
		if (version < -1 || version > 3)
			return false;
		return true;
	}

	private boolean wroteConnectCode = false;

	@Override
	public void write() throws IOException {
		if (wroteConnectCode) {
			// TODO:
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
		sb.append(", activity: ");
		sb.append(this.activity);
		sb.append("}");
		return sb.toString();
	}

}