package common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
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
	private ByteBuffer writeDataBuffer;
	private ByteBuffer readDataBuffer;
	private PacketManager packetManager;
	private static AtomicLong UID = new AtomicLong(0);
	private RpcPacket lastWritePacket;
	private ConcurrentLinkedQueue<RpcPacket> responsePackets;
	private SelectionKey selectionKey;

	protected AbstractConnection(PacketManager packetManager,
			SelectionKey selectionKey) {
		this.id = UID.getAndIncrement();
		this.selectionKey = selectionKey;
		readState = DataState.HEADER;
		readHeaderBuffer = ByteBuffer.allocate(RpcPacket.HEADER_SIZE);
		this.packetManager = packetManager;
		responsePackets = new ConcurrentLinkedQueue<RpcPacket>();
	}

	public long getId() {
		return id;
	}

	@Override
	public void close() throws IOException {
		this.socketChannel.close();

	}

	protected SocketChannel getSocketChannel() {
		return socketChannel;
	}

	@Override
	public void addResponsePacket(RpcPacket responsePacket)
			throws ClosedChannelException {
		this.responsePackets.add(responsePacket);
		this.selectionKey.interestOps(this.selectionKey.interestOps()
				| SelectionKey.OP_WRITE);
		System.out.println("add response packet:" + responsePacket.toString());
	}

	@Override
	public boolean isTimeOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int read() throws IOException {
		int readCount = 0;
		switch (readState) {
		case HEADER:
			readCount = this.socketChannel.read(readHeaderBuffer);
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
		case DATA:
			readCount = this.socketChannel.read(readDataBuffer);
			if (!readDataBuffer.hasRemaining()) {
				readDataBuffer.flip();
				try {
					lastReadPacket.setData(readDataBuffer.array());
					System.out.println("Read packet:"
							+ lastReadPacket.toString());
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
		return readCount;
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

		if (lastWritePacket == null) {
			lastWritePacket = responsePackets.remove();
			this.writeDataBuffer = ByteBuffer.allocate(lastWritePacket
					.getDataLength() + RpcPacket.HEADER_SIZE);
			this.writeDataBuffer.putLong(lastWritePacket.getCallId());
			this.writeDataBuffer.putLong(lastWritePacket.getChecksum());
			this.writeDataBuffer.putShort(lastWritePacket.getDataLength());
			this.writeDataBuffer.put(this.lastWritePacket.getData());
			this.writeDataBuffer.flip();
		}
		this.socketChannel.write(this.writeDataBuffer);
		if (!this.writeDataBuffer.hasRemaining()) {
			System.out.println("Write packet "
					+ this.lastWritePacket.toString() + " out!");
			this.lastWritePacket = null;
			// test if we need to unregister the write event.
			if (this.responsePackets.size() == 0)
				selectionKey.interestOps(SelectionKey.OP_READ);
		}

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
