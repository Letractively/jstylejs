package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import common.AbstractConnection;
import common.ConnectionCode;
import common.PacketManager;

class ServerConnection extends AbstractConnection {

	private boolean readConnectHeader = false;

	private boolean writeConnectCode = false;

	private byte protocol;

	private ConnectionCode connectionCode;

	private static final short version = 1;

	ServerConnection(SocketChannel clientChannel, PacketManager packetManager,
			SelectionKey selectionKey) {
		super(packetManager);
		this.selectionKey = selectionKey;
		this.socketChannel = clientChannel;
		setConnectionCode(ConnectionCode.OK);
	}

	@Override
	public int read() throws IOException {
		if (readConnectHeader) {
			// add threshold here.
			return super.read();
		} else {
			init();
			return 0;
		}
	}

	@Override
	public void write() throws IOException {
		if (writeConnectCode)
			super.write();
		else {
			// write response code.
			this.socketChannel.write(connectResponseBuffer);
			if (!this.connectResponseBuffer.hasRemaining()) {
				this.selectionKey.interestOps(SelectionKey.OP_READ);
				writeConnectCode = true;
				// try close this connection itself.
				switch (connectionCode) {
				case SERVICE_UNAVALIABLE:
					try {
						this.close();
					} catch (IOException e) {
						// Ignore.
					}
					break;
				case WRONG_VERSION:
					throw new IOException("Wrong version");

				default:
					break;
				}

			}
		}
	}

	private void setConnectionCode(ConnectionCode connectionCode) {
		this.connectionCode = connectionCode;
		this.connectResponseBuffer.clear();
		this.connectResponseBuffer.put(connectionCode.getCode());
		this.connectResponseBuffer.clear();
	}

	@Override
	protected void init() throws IOException {
		this.socketChannel.read(connectHeaderBuffer);
		if (!connectHeaderBuffer.hasRemaining()) {
			connectHeaderBuffer.flip();
			protocol = connectHeaderBuffer.get();
			short gotVersion = connectHeaderBuffer.getShort();
			if (gotVersion > version)
				setConnectionCode(ConnectionCode.WRONG_VERSION);
			this.selectionKey.interestOps(SelectionKey.OP_WRITE
					| SelectionKey.OP_READ);
			readConnectHeader = true;
		}
	}

	/**
	 * Deny to accept this connection into {@link PacketManager}.
	 */
	void denyToAccept() {
		this.selectionKey.interestOps(SelectionKey.OP_WRITE);
		setConnectionCode(ConnectionCode.SERVICE_UNAVALIABLE);
	}

	@Override
	public byte getProtocol() {
		return protocol;
	}

	@Override
	public int getVersion() {
		return version;
	}

}
