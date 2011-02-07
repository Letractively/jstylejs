package common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractConnection implements Connection {
	private enum DataState {
		HEADER, DATA;
	}

	private SocketChannel socketChannel;
	private long id;
	private RpcPacket lastReadPacket;
	private DataState readState;
	private ByteBuffer readHeaderBuffer;
	private ByteBuffer readDataBuffer;
	private PacketManager packetManager;
	private static AtomicLong UID = new AtomicLong(0);

	protected AbstractConnection(PacketManager packetManager) {
		this.id = UID.getAndIncrement();
		readState = DataState.HEADER;
		readHeaderBuffer = ByteBuffer.allocate(RpcPacket.HEADER_SIZE);
		this.packetManager = packetManager;
	}

	public long getId() {
		return id;
	}

	@Override
	public void close() throws IOException {

	}

	protected SocketChannel getSocketChannel() {
		return socketChannel;
	}

	@Override
	public boolean isTimeOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read() throws IOException {
		switch (readState) {
		case DATA:
			this.socketChannel.read(readHeaderBuffer);
			if (!readHeaderBuffer.hasRemaining()) {
				readHeaderBuffer.flip();
				long callId = readHeaderBuffer.getLong();
				long checksum = readHeaderBuffer.getLong();
				short dataLength = readHeaderBuffer.getShort();
				readDataBuffer = ByteBuffer.allocate(dataLength);
				lastReadPacket = new RpcPacket(this, callId, checksum,
						dataLength);
				readHeaderBuffer.clear();
				readState = DataState.DATA;
			}
			break;
		case HEADER:
			this.socketChannel.read(readDataBuffer);
			if (!readDataBuffer.hasRemaining()) {
				readDataBuffer.flip();
				try {
					lastReadPacket.setData(readDataBuffer.array());
					// add packet to manager
					this.packetManager.addReceived(lastReadPacket);
				} catch (ChecksumNotMatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				readState = DataState.HEADER;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void touch() {
		// TODO Auto-generated method stub

	}

	protected void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public void write() throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id: ");
		sb.append(this.id);
		sb.append(", address: ");
		sb.append(this.socketChannel.socket().toString());
		sb.append("}");
		return sb.toString();
	}

}
