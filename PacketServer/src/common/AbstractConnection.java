package common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractConnection implements Connection {
	private enum DataState {
		HEADER, DATA;
	}

	private static class ConnectionPacketManager {
		private AtomicInteger pendingIn;
		private AtomicInteger pendingOut;

		ConnectionPacketManager() {
			pendingIn = new AtomicInteger();
			pendingOut = new AtomicInteger();
		}

		public void incPendingIn() {
			pendingIn.incrementAndGet();
		}

		public void incPendingOut() {
			pendingOut.incrementAndGet();
		}

		public void decPendingIn() {
			pendingIn.decrementAndGet();

		}

		public void decPendingOut() {
			pendingOut.decrementAndGet();
		}

		public int getPendingIn() {
			return pendingIn.get();
		}

		public int getPendingOut() {
			return pendingOut.get();
		}
	}

	private static final int HEADER_LENGTH = 128;// protocol+version+reserved.
	private static final int CONNECT_RESPONSE_LENGTH = 128;// connection code
															// + reserved.
	protected ByteBuffer connectHeaderBuffer;
	/**
	 * Time out interval, in milli-seconds.
	 */
	private static final int TIME_OUT_INTERVAL = 1000 * 60;
	private long lastContact;
	protected SocketChannel socketChannel;
	private long id;
	private ConnectionPacketManager packetManager;
	private RpcPacket lastReadPacket;
	private DataState readState;
	private ByteBuffer readHeaderBuffer;
	private ByteBuffer writeDataBuffer;
	private ByteBuffer readDataBuffer;
	private PacketManager globalPacketManager;
	private static AtomicLong UID = new AtomicLong(0);
	private RpcPacket lastWritePacket;
	private Queue<RpcPacket> sendPackets;
	private HashMap<Long, RpcPacket> receivePackets;
	protected SelectionKey selectionKey;
	protected ByteBuffer connectResponseBuffer;

	protected AbstractConnection(PacketManager packetManager) {

		this.id = UID.getAndIncrement();
		readState = DataState.HEADER;
		readHeaderBuffer = ByteBuffer.allocate(RpcPacket.HEADER_SIZE);
		connectHeaderBuffer = ByteBuffer.allocate(HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer.allocate(CONNECT_RESPONSE_LENGTH);
		this.globalPacketManager = packetManager;
		sendPackets = new LinkedList<RpcPacket>();
		lastContact = System.currentTimeMillis();
		packetManager = new PacketManager();

	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void close() throws IOException {
		this.socketChannel.close();
		System.out.println(this.toString() + " closed!");
	}

	protected SocketChannel getSocketChannel() {
		return socketChannel;
	}

	@Override
	public void addSendPacket(RpcPacket packet) throws ClosedChannelException {
		synchronized (sendPackets) {
			this.sendPackets.add(packet);
		}
		this.selectionKey.interestOps(this.selectionKey.interestOps()
				| SelectionKey.OP_WRITE);
		System.out.println("add  packet " + packet.toString() + " to send");
	}

	@Override
	public boolean isTimeOut() {
		return System.currentTimeMillis() - lastContact > TIME_OUT_INTERVAL;
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
					this.packetManager.incPendingIn();
					// add packet to manager
					this.globalPacketManager.addReceived(lastReadPacket);
					// TODO: test add packet to response
					RpcPacket testPacket = new RpcPacket(this,
							this.lastReadPacket.getCallId() + 1,
							this.lastReadPacket.getChecksum(),
							this.lastReadPacket.getDataLength());
					testPacket.setData(this.lastReadPacket.getData());
					this.addSendPacket(testPacket);
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
		this.lastContact = System.currentTimeMillis();
	}

	@Override
	public void write() throws IOException {
		if (lastWritePacket == null) {
			synchronized (sendPackets) {

				lastWritePacket = sendPackets.remove();
			}
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
			synchronized (sendPackets) {
				if (this.sendPackets.size() == 0)
					selectionKey.interestOps(SelectionKey.OP_READ);
			}
		}

	}

	abstract protected void init() throws IOException;

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
		sb.append(", address: ");
		sb.append(this.socketChannel.socket().toString());
		sb.append("}");
		return sb.toString();
	}

}