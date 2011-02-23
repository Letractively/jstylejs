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
import java.util.logging.Logger;

import common.CRC16;
import common.ChecksumMatchException;
import common.Connection;
import common.ConnectionCode;
import common.ConnectionProtocol;
import common.Packet;
import common.PacketCarrier;
import common.PacketCounter;
import common.PacketException;
import common.PacketManager;
import common.PacketReader;
import common.RequestCode;

class ClientConnection implements Connection {

	private class PacketWriter {
		private PacketCarrier lastPacketCarrier;
		private ByteBuffer writeDataBuffer;
		private Queue<PacketCarrier> sendPackets;

		PacketWriter() {
			sendPackets = new LinkedList<PacketCarrier>();
		}

		void addSendPacket(PacketCarrier packetCarrier) throws IOException {
			synchronized (this.sendPackets) {
				this.sendPackets.add(packetCarrier);
			}
		}

		public void write() throws IOException {
			if (this.lastPacketCarrier == null) {
				synchronized (this.sendPackets) {

					this.lastPacketCarrier = this.sendPackets.remove();
				}
				byte code = this.lastPacketCarrier.getCode();
				short dataLength = this.lastPacketCarrier.getPacket()
						.getDataLength();
				byte[] data = this.lastPacketCarrier.getPacket().getData();
				short checksum = CRC16.compute(code, dataLength, data);
				this.writeDataBuffer = ByteBuffer
						.allocate(this.lastPacketCarrier.getPacket()
								.getDataLength()
								+ ConnectionProtocol.PACKET_HEADER_LENGTH);
				this.writeDataBuffer.put(code);
				this.writeDataBuffer.putShort(dataLength);
				this.writeDataBuffer.putShort(checksum);
				this.writeDataBuffer.put(data);
				this.writeDataBuffer.flip();
			}

			socketChannel.write(this.writeDataBuffer);
			if (!this.writeDataBuffer.hasRemaining()) {
				LOGGER.info("Write packet " + this.lastPacketCarrier.toString()
						+ " out!");
				packetCounter.writeOne();
				this.lastPacketCarrier = null;
				// test if we need to unregister the write event.
				synchronized (this.sendPackets) {
					if (this.sendPackets.size() == 0)
						selectionKey.interestOps(SelectionKey.OP_READ);
				}
			}

		}

		private void writeTestPacket(Packet packet) throws IOException {
			this.writeDataBuffer = ByteBuffer.allocate(packet.getDataLength()
					+ ConnectionProtocol.PACKET_HEADER_LENGTH);
			byte code = RequestCode.NORMAL.getCode();
			short dataLength = packet.getDataLength();
			byte[] data = packet.getData();

			this.writeDataBuffer.put(code);
			this.writeDataBuffer.putShort(dataLength);
			this.writeDataBuffer
					.putShort(CRC16.compute(code, dataLength, data));
			this.writeDataBuffer.put(packet.getData());
			this.writeDataBuffer.flip();
			while (this.writeDataBuffer.hasRemaining()) {
				socketChannel.write(writeDataBuffer);
			}
			LOGGER.info("Write packet out:" + packet.toString());
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
			InterruptedException, ChecksumMatchException, PacketException {
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
		byte[] bytes = new byte[344];
		Packet testPacket = new Packet(bytes);
		connection.writeTestPacket(testPacket);
		return;
	}

	private ByteBuffer connectHeaderBuffer;

	private ByteBuffer connectResponseBuffer;

	private AtomicBoolean gotErrorPacket;

	private long id;

	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private long lastContact;
	private Listener listener;
	private PacketCounter packetCounter;
	private PacketManager packetManager;
	private PacketReader packetReader;

	private PacketWriter packetWriter;
	private SelectionKey selectionKey;
	private SocketAddress serverSocket;
	private SocketChannel socketChannel;

	private Object thresholdLock;

	ClientConnection(SocketAddress serverSocket, PacketManager packetManager,
			Listener listener) {

		this.id = UID.getAndIncrement();
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);
		this.packetManager = packetManager;
		lastContact = System.currentTimeMillis();
		packetManager = new PacketManager();
		packetCounter = new PacketCounter();
		packetWriter = new PacketWriter();

		gotErrorPacket = new AtomicBoolean(false);

		this.listener = listener;
		this.serverSocket = serverSocket;
		this.connectHeaderBuffer.put(protocol);
		this.connectHeaderBuffer.putShort(version);
		this.connectHeaderBuffer.clear();
		thresholdLock = new Object();
	}

	private void addReceivedPacket(Packet packet) throws IOException {
		LOGGER.info("Read packet:" + packet.toString());
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
					LOGGER.info("Wati on two many pending packets");
					thresholdLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		addSendPacket1(new PacketCarrier(RequestCode.NORMAL.getCode(), packet));
	}

	private void addSendPacket1(PacketCarrier packetCarrier) throws IOException {
		this.packetWriter.addSendPacket(packetCarrier);
		this.selectionKey.interestOps(this.selectionKey.interestOps()
				| SelectionKey.OP_WRITE);
		LOGGER.info("add  packet " + packetCarrier.toString() + " to send");
	}

	@Override
	public void close() throws IOException {
		this.socketChannel.close();
		LOGGER.info(this.toString() + " closed!");
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

	private void gotErrorPacket() throws IOException {
		gotErrorPacket.set(true);
		// do not read any packet any more.
		this.socketChannel.socket().shutdownInput();
	}

	@Override
	public void init() throws IOException, ConnectException {
		// do connect to remote server.
		SocketChannel socketChannel = SocketChannel.open(this.serverSocket);
		this.socketChannel = socketChannel;
		// write connection header to remote server in blocked mode。
		this.socketChannel.write(connectHeaderBuffer);
		// read response in blocked mode.
		this.socketChannel.read(connectResponseBuffer);
		connectResponseBuffer.flip();
		ConnectionCode code = ConnectionCode.valueOf(connectResponseBuffer
				.get());
		if (code != ConnectionCode.OK)
			throw new ConnectException(code.name());
		socketChannel.configureBlocking(false);
		this.selectionKey = listener.register(this.socketChannel,
				SelectionKey.OP_READ, this);
		packetReader = new ClientPacketReader(this.socketChannel);
		LOGGER.info(this.toString() + " builded");
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
		int readCount;
		try {
			readCount = packetReader.read();
			if (packetReader.isReady()) {
				addReceivedPacket(packetReader.takeLastCarrier().getPacket());
			}
		} catch (ChecksumMatchException e) {
			stopWritePacket();
			throw new IOException(e);
		} catch (PacketException e) {
			stopWritePacket();
			throw new IOException(e);
		}
		return readCount;
	}

	private void stopWritePacket() {
		// dot net accept write event.
		this.selectionKey.interestOps(SelectionKey.OP_READ);
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
		if (gotErrorPacket.get()) {
			stopWritePacket();
			return;
		} else
			this.packetWriter.write();
		// add threshold control here.
		synchronized (thresholdLock) {
			if (!isTooManyPendingPackets())
				thresholdLock.notifyAll();
		}
	}

	private void writeTestPacket(Packet packet) throws IOException {
		this.packetWriter.writeTestPacket(packet);
	}

}