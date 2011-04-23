/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import org.creativor.rayson.client.Rayson;
import org.creativor.rayson.transport.common.CRC16;
import org.creativor.rayson.transport.common.ChecksumMatchException;
import org.creativor.rayson.transport.common.ConnectionProtocol;
import org.creativor.rayson.transport.common.ConnectionState;
import org.creativor.rayson.transport.common.Packet;
import org.creativor.rayson.transport.common.PacketConnection;
import org.creativor.rayson.transport.common.PacketCounter;
import org.creativor.rayson.transport.common.PacketException;
import org.creativor.rayson.transport.common.PacketReader;
import org.creativor.rayson.transport.common.PacketWithType;
import org.creativor.rayson.transport.common.RequestType;
import org.creativor.rayson.util.Log;

/**
 * 
 * @author Nick Zhang
 */
class RpcConnection extends PacketConnection {

	private class PacketWriter {
		private PacketWithType lastPacketCarrier;
		private Queue<PacketWithType> sendPackets;
		private ByteBuffer writeDataBuffer;
		private Lock packetsLock;

		PacketWriter() {
			sendPackets = new LinkedList<PacketWithType>();
			packetsLock = new ReentrantLock();
		}

		void addSendPacket(PacketWithType packetCarrier) throws IOException {
			packetsLock.lock();
			try {
				this.sendPackets.add(packetCarrier);
				if (this.sendPackets.size() == 1) {
					selectionKey.interestOps(selectionKey.interestOps()
							| SelectionKey.OP_WRITE);
					selectionKey.selector().wakeup();
				}
			} finally {
				packetsLock.unlock();
			}
		}

		public void write() throws IOException {
			if (this.lastPacketCarrier == null) {
				packetsLock.lock();
				try {
					this.lastPacketCarrier = this.sendPackets.remove();
				} finally {
					packetsLock.unlock();
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
				// LOGGER.info("Write packet " +
				// this.lastPacketCarrier.toString()
				// + " out!");
				touch();
				packetCounter.writeOne();
				this.lastPacketCarrier = null;
				// test if we need to unregister the write event.
				packetsLock.lock();
				try {
					if (this.sendPackets.isEmpty())
						selectionKey.interestOps(SelectionKey.OP_READ);
				} finally {
					packetsLock.unlock();
				}
			}
		}
	}

	private static Logger LOGGER = Log.getLogger();

	/**
	 * Time out interval, in milli-seconds.
	 */
	private static final int TIME_OUT_INTERVAL = 1000 * 60;

	private static final byte version = Rayson.getClientVersion();

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
	private Lock thresholdLock;

	private Condition tooManyPendingPackets;

	private AtomicBoolean removed;

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
		removed = new AtomicBoolean(false);

		packetManager = new PacketManager();
		packetCounter = new PacketCounter();
		packetWriter = new PacketWriter();

		readErrorPacket = new AtomicBoolean(false);

		this.listener = listener;
		this.serverSocket = serverSocket;
		this.connectHeaderBuffer.put(getProtocol().getType());
		this.connectHeaderBuffer.put(version);
		this.connectHeaderBuffer.clear();
		thresholdLock = new ReentrantLock();
		tooManyPendingPackets = thresholdLock.newCondition();
	}

	private void addReceivedPacket(Packet packet) throws IOException {
		// LOGGER.info("Read packet:" + packet.toString());
		this.touch();
		this.packetCounter.readOne();
		// add packet to manager
		this.packetManager.addReceived(packet);
	}

	@Override
	public void addSendPacket(Packet packet) throws IOException,
			InterruptedException {
		if (readErrorPacket.get()) {
			// do not accept new packet any more.
			return;
		}
		// add threshold control here.
		thresholdLock.lock();
		try {
			while (isTooManyPendingPackets()) {
				tooManyPendingPackets.await(100, TimeUnit.MILLISECONDS);
			}
			this.packetWriter.addSendPacket(new PacketWithType(
					RequestType.NORMAL.getType(), packet));

		} finally {
			thresholdLock.unlock();
		}
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

	/**
	 * @return True if this connection is closed.
	 */
	public synchronized boolean isClosed() {
		return closed.get();
	}

	@Override
	public long getId() {
		return id;
	}

	public SocketAddress getServerSocket() {
		return serverSocket;
	}

	@Override
	public byte getVersion() {

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
		try {
			this.selectionKey = listener.accept(this.socketChannel, this);
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
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

		// The following line is used to improve the performance ,but make the
		// threshold is not thread safe. In fact, we
		// do not need an extremely threshold.
		if (pendingPacketCount() + 3 < ConnectionProtocol.MAX_PENDING_PACKETS)
			return;

		// add threshold control here.
		thresholdLock.lock();
		try {
			if (!isTooManyPendingPackets())
				tooManyPendingPackets.signalAll();
		} finally {
			thresholdLock.unlock();
		}
	}

	/**
	 * Notify other thread that this connection is removed from
	 * {@link ConnectionManager}.
	 */
	public void notifyRemoved() {
		synchronized (removed) {
			removed.set(true);
			removed.notifyAll();
		}
	}

	/**
	 * Wait until this connection is removed from {@link ConnectionManager}.
	 * 
	 * @throws InterruptedException
	 */
	public void waitForRemoved() throws InterruptedException {

		synchronized (removed) {
			while (!removed.get()) {
				removed.wait();
			}
		}

	}
}