package org.rayson.transport.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.transport.api.Connection;
import org.rayson.transport.common.ConnectionState;
import org.rayson.transport.common.ProtocolType;

class PendingConnection implements Connection {
	private static AtomicLong UID = new AtomicLong(0);
	private ByteBuffer connectpProtocolBuffer;
	private ByteBuffer connectResponseBuffer;
	private long id;
	private SelectionKey selectionKey;
	private TransportServer server;
	private SocketChannel socketChannel;

	public PendingConnection(TransportServer server,
			SocketChannel clientChannel, SelectionKey selectionKey) {
		this.id = UID.getAndIncrement();
		this.server = server;
		this.selectionKey = selectionKey;
		this.socketChannel = clientChannel;
		connectpProtocolBuffer = ByteBuffer.allocate(1);
		connectResponseBuffer = ByteBuffer.allocate(1);
		setConnectionState(ConnectionState.OK);
	}

	@Override
	public void close() throws IOException {
		this.socketChannel.close();
	}

	/**
	 * Deny to accept this connection into {@link PacketManager}.
	 */
	void denyToAccept() {
		// no need to read.
		this.selectionKey.interestOps(SelectionKey.OP_WRITE);
		setConnectionState(ConnectionState.SERVICE_UNAVALIABLE);
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public ProtocolType getProtocol() {
		return ProtocolType.UNKNOWN;
	}

	@Override
	public int read() throws IOException {
		int readCount = this.socketChannel.read(connectpProtocolBuffer);
		if (!connectpProtocolBuffer.hasRemaining()) {
			connectpProtocolBuffer.flip();
			ProtocolType protocolType = ProtocolType
					.valueOf(connectpProtocolBuffer.get());
			switch (protocolType) {
			case PING: {
				setConnectionState(ConnectionState.OK);
				this.selectionKey.interestOps(SelectionKey.OP_WRITE);
			}
				break;
			case RPC: {
				RpcConnection rpcConnection = new RpcConnection(id,
						socketChannel, this.server.getPacketManager(),
						selectionKey);
				this.server.getConnectionManager().accept(id, rpcConnection);
				// set acctchment to new connection.
				this.selectionKey.attach(rpcConnection);
			}
				break;
			case STREAM: {
				StreamConnection streamConnection = new StreamConnection(id,
						socketChannel, selectionKey);
				this.server.getConnectionManager().accept(id, streamConnection);
				// set acctchment to new connection.
				this.selectionKey.attach(streamConnection);
			}
				break;
			default:
				throw new IOException("Unsupported protocol");
			}
		}
		return readCount;
	}

	private void setConnectionState(ConnectionState connectionState) {
		this.connectResponseBuffer.put(connectionState.getState());
		this.connectResponseBuffer.clear();
	}

	@Override
	public void write() throws IOException {
		// Write back response code.
		this.socketChannel.write(connectResponseBuffer);
		if (!connectResponseBuffer.hasRemaining()) {
			// remove this pending connection.
			this.server.getConnectionManager().removePending(this);
			this.close();

		}
	}

	@Override
	public short getVersion() {
		return -1;
	}

}
