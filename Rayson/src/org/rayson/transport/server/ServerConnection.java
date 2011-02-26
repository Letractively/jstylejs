package org.rayson.transport.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rayson.transport.common.CRC16;
import org.rayson.transport.common.ChecksumMatchException;
import org.rayson.transport.common.Connection;
import org.rayson.transport.common.ConnectionCode;
import org.rayson.transport.common.ConnectionProtocol;
import org.rayson.transport.common.Packet;
import org.rayson.transport.common.PacketCarrier;
import org.rayson.transport.common.PacketCounter;
import org.rayson.transport.common.PacketException;
import org.rayson.transport.common.PacketManager;
import org.rayson.transport.common.PacketReader;
import org.rayson.transport.common.ResponseCode;




class ServerConnection implements Connection {

	private class PacketWriter {
		private PacketCarrier lastPacketCarrier;
		private ByteBuffer writeDataBuffer;

		private void write() throws IOException {
			if (this.lastPacketCarrier == null) {
				synchronized (sendPackets) {

					this.lastPacketCarrier = sendPackets.remove();
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

			ServerConnection.this.socketChannel.write(this.writeDataBuffer);
			if (!this.writeDataBuffer.hasRemaining()) {
				LOGGER.info("Write packet " + this.lastPacketCarrier.toString()
						+ " out!");
				touch();
				packetCounter.writeOne();
				this.lastPacketCarrier = null;
				if (gotErrorPacket.get())
					return;
				// we need to unregister the write event.
				synchronized (sendPackets) {
					if (sendPackets.size() == 0)
						selectionKey.interestOps(SelectionKey.OP_READ);
				}
			}

		}
	}

	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static AtomicLong UID = new AtomicLong(0);
	private static final short version = 1;

	private AtomicBoolean closed;

	private ByteBuffer connectHeaderBuffer;
	private ConnectionCode connectionCode;
	private ByteBuffer connectResponseBuffer;

	private ByteBuffer errorResponseBuffer;

	private AtomicBoolean gotErrorPacket;
	private long id;
	private volatile long lastContact;
	private PacketCounter packetCounter;
	private PacketManager packetManager;

	private PacketReader packetReader;

	private PacketWriter packetWriter;

	private byte protocol;

	private boolean readedConnectHeader = false;
	private SelectionKey selectionKey;
	private Queue<PacketCarrier> sendPackets;
	private SocketChannel socketChannel;
	private boolean wroteConnectCode = false;

	ServerConnection(SocketChannel clientChannel, PacketManager packetManager,
			SelectionKey selectionKey) {
		this.id = UID.getAndIncrement();

		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);
		this.packetManager = packetManager;
		closed = new AtomicBoolean(false);
		sendPackets = new LinkedList<PacketCarrier>();
		lastContact = System.currentTimeMillis();
		packetCounter = new PacketCounter();
		gotErrorPacket = new AtomicBoolean(false);
		this.selectionKey = selectionKey;
		this.socketChannel = clientChannel;
		setConnectionCode(ConnectionCode.OK);
		packetWriter = new PacketWriter();
		packetReader = new ServerPacketReader(this.socketChannel);
	}

	protected void addReceivedPacket(Packet packet) throws IOException {
		LOGGER.info("Read packet:" + packet.toString());
		this.touch();
		this.packetCounter.readOne();
		// add packet to manager
		this.packetManager.addReceived(packet);
		// TODO: test add packet to response
		this.addReqeustPacket(packet);
	}

	@Override
	public void addReqeustPacket(Packet packet) throws IOException {
		synchronized (sendPackets) {
			this.sendPackets.add(new PacketCarrier(ResponseCode.OK.getCode(),
					packet));
		}
		this.selectionKey.interestOps(this.selectionKey.interestOps()
				| SelectionKey.OP_WRITE);
		this.selectionKey.selector().wakeup();
		LOGGER.info("add  packet " + packet.toString() + " to send");
	}

	@Override
	public synchronized void close() throws IOException {
		if (!closed.compareAndSet(false, true))
			return;
		this.socketChannel.close();
		LOGGER.info(this.toString() + " closed!");
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

	private void gotErrorPacket(ResponseCode responseCode) throws IOException {
		gotErrorPacket.set(true);
		// Initialize error response buffer.
		byte code = responseCode.getCode();
		short dataLength = 0;
		byte[] data = new byte[dataLength];
		short checksum = CRC16.compute(code, dataLength, data);
		errorResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.PACKET_HEADER_LENGTH);
		errorResponseBuffer.put(code);
		errorResponseBuffer.putShort(dataLength);
		errorResponseBuffer.putShort(checksum);
		errorResponseBuffer.flip();
		// do not accept any packet any more.
		this.selectionKey.interestOps(SelectionKey.OP_WRITE);
		this.socketChannel.socket().shutdownInput();
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
			readedConnectHeader = true;
		}
	}

	@Override
	public boolean isTimeOut() {
		return System.currentTimeMillis() - lastContact > ConnectionProtocol.TIME_OUT_INTERVAL;
	}

	@Override
	public int pendingPacketCount() {
		return (int) (this.packetCounter.readCount() - this.packetCounter
				.writeCount());
	}

	@Override
	public int read() throws IOException {

		if (readedConnectHeader) {
			// add threshold control here.
			if (this.pendingPacketCount() >= ConnectionProtocol.MAX_PENDING_PACKETS)
				return 0;
			int readCount = 0;
			try {
				readCount = packetReader.read();
			} catch (ChecksumMatchException e) {
				LOGGER.log(Level.SEVERE, "Read packet error: " + e.getMessage());
				gotErrorPacket(ResponseCode.CRC_ERROR);
				return 0;
			} catch (PacketException e) {
				LOGGER.log(Level.SEVERE, "Read packet error: " + e.getMessage());
				gotErrorPacket(ResponseCode.UNKNOW_REQUEST_CODE);
				return 0;
			}
			if (packetReader.isReady()) {
				addReceivedPacket(packetReader.takeLastCarrier().getPacket());
			}
			return readCount;
		} else {
			init();
			return 0;
		}
	}

	private void setConnectionCode(ConnectionCode connectionCode) {
		this.connectionCode = connectionCode;
		this.connectResponseBuffer.clear();
		this.connectResponseBuffer.put(connectionCode.getCode());
		this.connectResponseBuffer.clear();
	}

	private boolean shouldWriteErrorResponse() {
		return gotErrorPacket.get() && this.sendPackets.isEmpty();
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
		sb.append(lastContact);
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
	public void touch() {
		this.lastContact = System.currentTimeMillis();
	}

	@Override
	public void write() throws IOException {
		if (wroteConnectCode) {
			if (shouldWriteErrorResponse()) {
				writeErrorResponse();
			} else {
				packetWriter.write();
			}

		} else {
			// write connection response code.
			this.socketChannel.write(connectResponseBuffer);
			if (!this.connectResponseBuffer.hasRemaining()) {
				this.selectionKey.interestOps(SelectionKey.OP_READ);
				wroteConnectCode = true;
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

	private void writeErrorResponse() throws IOException {
		this.socketChannel.write(errorResponseBuffer);
		if (!errorResponseBuffer.hasRemaining()) {
			// unregister the write event, and make this connection can read
			// EOF.
			this.selectionKey.interestOps(SelectionKey.OP_READ);
			this.socketChannel.socket().shutdownOutput();
		}
	}
}