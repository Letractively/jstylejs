package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
import common.ResponseCode;

class ClientConnection implements Connection {
	private enum PacketReadState {
		RESPONSE_CODE, DATA, HEADER;
	}

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

	private static final byte protocol = 1;
	/**
	 * Time out interval, in milli-seconds.
	 */
	private static final int TIME_OUT_INTERVAL = 1000 * 60;

	private static AtomicLong UID = new AtomicLong(0);

	private static final short version = 1;

	public static void main(String[] args) throws IOException,
			InterruptedException, ChecksumNotMatchException {
		PacketManager packetManager = new PacketManager();
		SocketAddress serverSocket = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);
		Listener listener = new Listener();
		listener.start();
		ClientConnection connection = new ClientConnection(serverSocket,
				packetManager, listener);
		try {
			connection.init();
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Packet testPacket = new Packet(324324234, (short) 344);
		testPacket.setData(new byte[344]);
		connection.writeTestPacket(testPacket);
		return;
	}

	private ByteBuffer connectHeaderBuffer;

	private ByteBuffer connectResponseBuffer;

	private long id;

	private long lastContact;
	private Packet lastWritePacket;
	private Listener listener;
	private PacketCounter packetCounter;
	private AtomicBoolean packetErrorOccurred;
	private PacketManager packetManager;
	private ByteBuffer readDataBuffer;
	private ByteBuffer readHeaderBuffer;
	private ByteBuffer readResponseCodeBuffer;
	private PacketReadState readState;
	private ReceivedPacketWrapper receivedPacketWrapper;
	private SelectionKey selectionKey;
	private Queue<Packet> sendPackets;
	private SocketAddress serverSocket;
	private SocketChannel socketChannel;
	private Object thresholdLock;

	private ByteBuffer writeDataBuffer;

	ClientConnection(SocketAddress serverSocket, PacketManager packetManager,
			Listener listener) {

		this.id = UID.getAndIncrement();
		readState = PacketReadState.RESPONSE_CODE;
		readHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.PACKET_HEADER_SIZE);
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);
		readResponseCodeBuffer = ByteBuffer
				.allocate(ConnectionProtocol.PACKET_RESPONSE_CODE_SIZE);
		this.packetManager = packetManager;
		sendPackets = new LinkedList<Packet>();
		lastContact = System.currentTimeMillis();
		packetManager = new PacketManager();
		packetCounter = new PacketCounter();
		receivedPacketWrapper = new ReceivedPacketWrapper();
		packetErrorOccurred = new AtomicBoolean(false);

		this.listener = listener;
		this.serverSocket = serverSocket;
		this.connectHeaderBuffer.put(protocol);
		this.connectHeaderBuffer.putShort(version);
		this.connectHeaderBuffer.clear();
		thresholdLock = new Object();
	}

	private void addReceivedPacket(long checksum, byte[] data)
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
		// add threshold control here.
		synchronized (thresholdLock) {
			while (isTooManyPendingPackets()) {
				try {
					System.out.println("Wati on two many pending packets");
					thresholdLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		addSendPacket1(packet);
	}

	private void addSendPacket1(Packet packet) throws IOException {
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
	public void init() throws IOException, ConnectException {
		// do connect to remote server.
		SocketChannel socketChannel = SocketChannel.open(this.serverSocket);
		this.socketChannel = socketChannel;
		// write connection header to remote server.
		this.socketChannel.write(connectHeaderBuffer);
		// read response.
		this.socketChannel.read(connectResponseBuffer);
		connectResponseBuffer.flip();
		ConnectionCode code = ConnectionCode.valueOf(connectResponseBuffer
				.get());
		if (code != ConnectionCode.OK)
			throw new ConnectException(code.name());
		socketChannel.configureBlocking(false);
		this.selectionKey = listener.register(this.socketChannel,
				SelectionKey.OP_READ, this);
		System.out.println(this.toString() + " builded");
	}

	@Override
	public boolean isTimeOut() {
		return System.currentTimeMillis() - lastContact > TIME_OUT_INTERVAL;
	}

	private boolean isTooManyPendingPackets() {
		return this.pendingPacketCount() > ConnectionProtocol.MAX_PENDING_PACKETS;
	}

	@Override
	public int pendingPacketCount() {
		return (int) (this.packetCounter.writeCount() - this.packetCounter
				.readCount());
	}

	@Override
	public int read() throws IOException {
		int readCount = 0;
		switch (readState) {
		case RESPONSE_CODE:
			readCount = this.socketChannel.read(readResponseCodeBuffer);
			if (!readResponseCodeBuffer.hasRemaining()) {
				readResponseCodeBuffer.flip();
				ResponseCode responseCode = ResponseCode
						.valueOf(readResponseCodeBuffer.get());
				if (responseCode != ResponseCode.OK) {
					throw new IOException("Get packet response code: "
							+ responseCode.name());
				}
				readResponseCodeBuffer.clear();
				readState = PacketReadState.HEADER;
			}
			break;
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
				readState = PacketReadState.RESPONSE_CODE;
			}
			break;
		default:
			break;
		}
		return readCount;
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
		writePacket();
		// add threshold control here.
		synchronized (thresholdLock) {
			if (!isTooManyPendingPackets())
				thresholdLock.notifyAll();
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

	private void writeTestPacket(Packet rpcPacket) throws IOException {
		this.writeDataBuffer = ByteBuffer.allocate(rpcPacket.getDataLength()
				+ ConnectionProtocol.PACKET_HEADER_SIZE);
		this.writeDataBuffer.putLong(rpcPacket.getChecksum());
		this.writeDataBuffer.putShort(rpcPacket.getDataLength());
		this.writeDataBuffer.put(rpcPacket.getData());
		this.writeDataBuffer.flip();
		while (this.writeDataBuffer.hasRemaining()) {
			this.socketChannel.write(writeDataBuffer);
		}
		System.out.println("Write packet out:" + rpcPacket.toString());
	}

}