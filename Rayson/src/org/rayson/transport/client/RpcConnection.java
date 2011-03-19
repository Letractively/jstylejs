package org.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.rayson.transport.common.PacketConnection;
import org.rayson.transport.common.CRC16;
import org.rayson.transport.common.ChecksumMatchException;
import org.rayson.transport.common.ConnectionProtocol;
import org.rayson.transport.common.ConnectionState;
import org.rayson.transport.common.Packet;
import org.rayson.transport.common.PacketCounter;
import org.rayson.transport.common.PacketException;
import org.rayson.transport.common.PacketReader;
import org.rayson.transport.common.PacketWithType;
import org.rayson.transport.common.RequestType;
import org.rayson.util.Log;

class RpcConnection extends PacketConnection {

	private class PacketWriter {
		private PacketWithType lastPacketCarrier;
		private Queue<PacketWithType> sendPackets;
		private ByteBuffer writeDataBuffer;

		PacketWriter() {
			sendPackets = new LinkedList<PacketWithType>();
		}

		void addSendPacket(PacketWithType packetCarrier) throws IOException {
			synchronized (this.sendPackets) {
				this.sendPackets.add(packetCarrier);
			}
		}

		public void write() throws IOException {
			if (this.lastPacketCarrier == null) {
				synchronized (this.sendPackets) {

					this.lastPacketCarrier = this.sendPackets.remove();
				}
				byte code = this.lastPacketCarrier.getType();
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
				touch();
				packetCounter.writeOne();
				this.lastPacketCarrier = null;
				// test if we need to unregister the write event.
				synchronized (this.sendPackets) {
					if (this.sendPackets.size() == 0)
						selectionKey.interestOps(SelectionKey.OP_READ);
				}
			}

		}

	}

	private static Logger LOGGER = Log.getLogger();

	/**
	 * Time out interval, in milli-seconds.
	 */
	private static final int TIME_OUT_INTERVAL = 1000 * 60;

	private static final short version = 1;

	private AtomicBoolean closed;

	private ByteBuffer connectHeaderBuffer;

	private ByteBuffer connectResponseBuffer;
	private long id;
	private Listener listener;
	private PacketCounter packetCounter;
	private PacketManager packetManager;
	private PacketReader packetReader;

	private PacketWriter packetWriter;
	private AtomicBoolean readErrorPacket;
	private SelectionKey selectionKey;
	private SocketAddress serverSocket;

	private SocketChannel socketChannel;
	private Object thresholdLock;

	RpcConnection(SocketAddress serverSocket, PacketManager packetManager,
			Listener listener) {
		super();
		this.id = ConnectionManager.getNextConnectionId();
		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);
		this.packetManager = packetManager;
		closed = new AtomicBoolean(false);

		packetManager = new PacketManager();
		packetCounter = new PacketCounter();
		packetWriter = new PacketWriter();

		readErrorPacket = new AtomicBoolean(false);

		this.listener = listener;
		this.serverSocket = serverSocket;
		this.connectHeaderBuffer.put(getProtocol().getType());
		this.connectHeaderBuffer.putShort(version);
		this.connectHeaderBuffer.clear();
		thresholdLock = new Object();
	}

	private void addReceivedPacket(Packet packet) throws IOException {
		LOGGER.info("Read packet:" + packet.toString());
		this.touch();
		this.packetCounter.readOne();
		// add packet to manager
		this.packetManager.addReceived(packet);
	}

	private void addReqeustPacket1(PacketWithType packetWithType)
			throws IOException {
		this.packetWriter.addSendPacket(packetWithType);
		this.selectionKey.interestOps(this.selectionKey.interestOps()
				| SelectionKey.OP_WRITE);
		this.selectionKey.selector().wakeup();
		LOGGER.info("add  packet " + packetWithType.toString() + " to send");
	}

	@Override
	public void addSendPacket(Packet packet) throws IOException {
		if (readErrorPacket.get()) {
			// do not accept new packet any more.
			return;
		}
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
		addReqeustPacket1(new PacketWithType(RequestType.NORMAL.getType(),
				packet));
	}

	@Override
	public synchronized void close() throws IOException {
		if (!closed.compareAndSet(false, true))
			return;
		try {
			this.socketChannel.close();
		} finally {
			TransportClient.getSingleton().notifyConnectionClosed(this);
		}
		LOGGER.info(this.toString() + " closed!");
	}

	@Override
	public long getId() {
		return id;
	}

	public SocketAddress getServerSocket() {
		return serverSocket;
	}

	@Override
	public short getVersion() {

		return version;
	}

	@Override
	public void init() throws IOException, ConnectException {
		// do connect to remote server.
		SocketChannel socketChannel = SocketChannel.open(this.serverSocket);
		this.socketChannel = socketChannel;
		// write connection header to remote server in blocked mode
		this.socketChannel.write(connectHeaderBuffer);
		// read response in blocked mode.
		this.socketChannel.read(connectResponseBuffer);
		connectResponseBuffer.flip();
		ConnectionState state = ConnectionState.valueOf(connectResponseBuffer
				.get());
		if (state != ConnectionState.OK)
			throw new ConnectException(state.name());
		socketChannel.configureBlocking(false);
		this.selectionKey = listener.register(this.socketChannel,
				SelectionKey.OP_READ, this);
		packetReader = new ClientPacketReader(this.socketChannel);
		LOGGER.info(this.toString() + " builded");
	}

	@Override
	protected long getTimeoutInterval() {
		return TIME_OUT_INTERVAL;
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
				addReceivedPacket(packetReader.takeLastWithType().getPacket());
			}
		} catch (ChecksumMatchException e) {
			readErrorPacket();
			throw new IOException(e);
		} catch (PacketException e) {
			readErrorPacket();
			throw new IOException(e);
		}
		return readCount;
	}

	private void readErrorPacket() throws IOException {
		readErrorPacket.set(true);
		// do not read any packet any more.
		this.socketChannel.socket().shutdownInput();
		// dot not accept write event.
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
		sb.append(", version: ");
		sb.append(this.getVersion());
		sb.append(", last contact: ");
		sb.append(getLastContactTime());
		sb.append(", packet counter: ");
		sb.append(this.packetCounter.toString());
		sb.append(", pending packets: ");
		sb.append(this.pendingPacketCount());
		sb.append(", address: ");
		sb.append(this.socketChannel.socket().toString());
		sb.append("}");
		return sb.toString();
	}

	@Override
	public void write() throws IOException {
		if (readErrorPacket.get()) {
			// do not write any more.
			return;
		} else
			this.packetWriter.write();
		// add threshold control here.
		synchronized (thresholdLock) {
			if (!isTooManyPendingPackets())
				thresholdLock.notifyAll();
		}
	}

}