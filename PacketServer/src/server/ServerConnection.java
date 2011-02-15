package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import common.AbstractConnection;
import common.ConnectException;
import common.ConnectionCode;
import common.PacketManager;

class ServerConnection extends AbstractConnection {

	private boolean readHeader = false;

	private boolean writeConnectCode = false;

	private boolean denyed = false;
	private byte protocol;

	private ByteBuffer connectionCodeBuffer;

	private static final short version = 1;

	ServerConnection(SocketChannel clientChannel, PacketManager packetManager,
			SelectionKey selectionKey) {
		super(packetManager);
		this.selectionKey = selectionKey;
		this.socketChannel = clientChannel;
		connectionCodeBuffer = ByteBuffer.allocate(1);
	}

	@Override
	public int read() throws IOException {
		if (readHeader)
			return super.read();
		else {
			try {
				connect();
			} catch (ConnectException e) {
				throw new IOException(e);
			}
			return 0;
		}
	}

	@Override
	public void write() throws IOException {
		if (writeConnectCode)
			super.write();
		else {
			// write response code.
			this.socketChannel.write(connectionCodeBuffer);
			if (!this.connectionCodeBuffer.hasRemaining()) {
				this.selectionKey.interestOps(SelectionKey.OP_READ);
				writeConnectCode = true;
				// try close this connection itself.
				if (denyed) {
					try {
						this.close();
					} catch (IOException e) {
						// Ignore.
					}
				}
			}
		}
	}

	@Override
	protected void connect() throws IOException, ConnectException {
		this.socketChannel.read(connectHeaderBuffer);
		if (!connectHeaderBuffer.hasRemaining()) {
			connectHeaderBuffer.flip();
			protocol = connectHeaderBuffer.get();
			short gotVersion = connectHeaderBuffer.getShort();
			if (gotVersion > version)
				throw new ConnectException("Got version " + gotVersion
						+ " larger than sever version " + version);
			this.selectionKey.interestOps(SelectionKey.OP_WRITE
					| SelectionKey.OP_READ);
			connectionCodeBuffer.put(ConnectionCode.OK.getCode());
			connectionCodeBuffer.flip();
			readHeader = true;
		}
	}

	void denyed() {
		this.selectionKey.interestOps(SelectionKey.OP_WRITE);
		connectionCodeBuffer.put(ConnectionCode.SERVICE_UNAVALIABLE.getCode());
		connectionCodeBuffer.flip();
		denyed = true;
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
