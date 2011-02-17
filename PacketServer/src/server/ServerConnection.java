package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import common.ChecksumNotMatchException;
import common.Connection;
import common.ConnectionCode;
import common.ConnectionProtocol;
import common.Packet;
import common.PacketCounter;
import common.PacketManager;
import common.PacketReadState;

class ServerConnection implements Connection {
	private static class ReceivedPacketWrapper {
		private long checksum;
		private byte[] data;

		ReceivedPacketWrapper() {

		}

		void setChecksum(long checksum) {
			this.checksum = checksum;
		}

		void setData(byte[] data) {
			this.data = data;

		}
	}

	/**
	 * Time out interval, in milli-seconds.
	 */
	private static final int TIME_OUT_INTERVAL = 1000 * 60;
	private static AtomicLong UID = new AtomicLong(0);
	private static final short version = 1;
	private ByteBuffer connectHeaderBuffer;

	private ConnectionCode connectionCode;
	private ByteBuffer connectResponseBuffer;
	private long id;
	private long lastContact;

	private Packet lastWritePacket;

	private PacketCounter packetCounter;

	private AtomicBoolean packetErrorOccurred;

	private PacketManager packetManager;
	private byte protocol;

	private boolean readConnectHeader = false;

	private ByteBuffer readDataBuffer;
	private ByteBuffer readHeaderBuffer;
	private PacketReadState readState;
	private ReceivedPacketWrapper receivedPacketWrapper;
	private SelectionKey selectionKey;
	private Queue<Packet> sendPackets;
	private SocketChannel socketChannel;
	private boolean writeConnectCode = false;
	private ByteBuffer writeDataBuffer;

	ServerConnection(SocketChannel clientChannel, PacketManager packetManager,
			SelectionKey selectionKey) {
		this.id = UID.getAndIncrement();
		readState = PacketReadState.HEADER;
		readHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.PACKET_HEADER_SIZE);
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);
		this.packetManager = packetManager;
		sendPackets = new LinkedList<Packet>();
		lastContact = System.currentTimeMillis();
		packetCounter = new PacketCounter();
		receivedPacketWrapper = new ReceivedPacketWrapper();
		packetErrorOccurred = new AtomicBoolean(false);
		this.selectionKey = selectionKey;
		this.socketChannel = clientChannel;
		setConnectionCode(ConnectionCode.OK);
	}

	protected void addReceivedPacket(long checksum, byte[] data)
			throws IOException {
		Packet packet = new Packet(checksum, (short) data.length);
		try {
			packet.setData(data);
		} catch (ChecksumNotMatchException e) {
			e.printStackTrace();
			packetErrorOccurred.set(true);
		}
		System.out.println("Read packet:" + packet.toString());
		this.packetCounter.readOne();
		// add packet to manager
		this.packetManager.addReceived(packet);
		// TODO: test add packet to response
		this.addSendPacket(packet);
	}

	@Override
	public void addSendPacket(Packet packet) throws IOException {
		synchronized (sendPackets) {
			this.sendPackets.add(packet);
		}
		this.selectionKey.interestOps(this.selectionKey.interestOps()
				| SelectionKey.OP_WRITE);
		System.out.println("add  packet " + packet.toString() + " to send");
	}

	@Override
	public void close() throws IOException {
		this.socketChannel.close();
		System.out.println(this.toString() + " closed!");
	}

	/**
	 * Deny to accept this connection into {@link PacketManager}.
	 */
	void denyToAccept() {
		this.selectionKey.interestOps(SelectionKey.OP_WRITE);
		setConnectionCode(ConnectionCode.SERVICE_UNAVALIABLE);
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public byte getProtocol() {
		return protocol;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void init() throws IOException {
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

	@Override
	public boolean isTimeOut() {
		return System.currentTimeMillis() - lastContact > TIME_OUT_INTERVAL;
	}

	@Override
	public int pendingPacketCount() {
		return (int) (this.packetCounter.readCount() - this.packetCounter
				.writeCount());
	}

	@Override
	public int read() throws IOException {
		if (readConnectHeader) {
			// add threshold control here.
			if (this.pendingPacketCount() >= ConnectionProtocol.MAX_PENDING_PACKETS)
				return 0;
			return readPacket();
		} else {
			init();
			return 0;
		}
	}

	private int readPacket() throws IOException {
		int readCount = 0;
		switch (readState) {
		case HEADER:
			readCount = this.socketChannel.read(readHeaderBuffer);
			if (!readHeaderBuffer.hasRemaining()) {
				readHeaderBuffer.flip();
				long checksum = readHeaderBuffer.getLong();
				short dataLength = readHeaderBuffer.getShort();
				readDataBuffer = ByteBuffer.allocate(dataLength);
				receivedPacketWrapper.setChecksum(checksum);
				readHeaderBuffer.clear();
				readState = PacketReadState.DATA;
			}
			break;
		case DATA:
			readCount = this.socketChannel.read(readDataBuffer);
			if (!readDataBuffer.hasRemaining()) {
				readDataBuffer.flip();
				receivedPacketWrapper.setData(readDataBuffer.array());
				addReceivedPacket(receivedPacketWrapper.checksum,
						receivedPacketWrapper.data);
				readState = PacketReadState.HEADER;
			}
			break;
		default:
			break;
		}
		return readCount;
	}

	private void setConnectionCode(ConnectionCode connectionCode) {
		this.connectionCode = connectionCode;
		this.connectResponseBuffer.clear();
		this.connectResponseBuffer.put(connectionCode.getCode());
		this.connectResponseBuffer.clear();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id: ");
		sb.append(this.id);
		sb.append(", protocol: ");
		sb.append(this.getProtocol());
		sb.append(", packet counter: ");
		sb.append(this.packetCounter.toString());
		sb.append(", pending packets: ");
		sb.append(this.pendingPacketCount());
		sb.append(", version: ");
		sb.append(this.getVersion());
		sb.append(", address: ");
		sb.append(this.socketChannel.socket().toString());
		sb.append("}");
		return sb.toString();
	}

	@Override
	public void touch() {
		this.lastContact = System.currentTimeMillis();
	}

	@Override
	public void write() throws IOException {
		if (writeConnectCode)
			writePacket();
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

	private void writePacket() throws IOException {
		if (lastWritePacket == null) {
			synchronized (sendPackets) {

				lastWritePacket = sendPackets.remove();
			}
			this.writeDataBuffer = ByteBuffer.allocate(lastWritePacket
					.getDataLength() + ConnectionProtocol.PACKET_HEADER_SIZE);
			this.writeDataBuffer.putLong(lastWritePacket.getChecksum());
			this.writeDataBuffer.putShort(lastWritePacket.getDataLength());
			this.writeDataBuffer.put(this.lastWritePacket.getData());
			this.writeDataBuffer.flip();
		}

		this.socketChannel.write(this.writeDataBuffer);
		if (!this.writeDataBuffer.hasRemaining()) {
			System.out.println("Write packet "
					+ this.lastWritePacket.toString() + " out!");
			this.packetCounter.writeOne();
			this.lastWritePacket = null;
			// test if we need to unregister the write event.
			synchronized (sendPackets) {
				if (this.sendPackets.size() == 0)
					selectionKey.interestOps(SelectionKey.OP_READ);
			}
		}

	}
}