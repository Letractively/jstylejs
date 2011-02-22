package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import common.ChecksumErrorException;
import common.Connection;
import common.ConnectionCode;
import common.ConnectionProtocol;
import common.Packet;
import common.PacketCounter;
import common.PacketManager;
import common.ResponseCode;

class ServerConnection implements Connection {

	private class PacketReader {
		private ByteBuffer readDataBuffer;
		private ByteBuffer readHeaderBuffer;
		private PacketReadState readState;
		private ReceivedPacketWrapper receivedPacketWrapper;

		PacketReader() {
			receivedPacketWrapper = new ReceivedPacketWrapper();
			readState = PacketReadState.HEADER;
			readHeaderBuffer = ByteBuffer
					.allocate(ConnectionProtocol.PACKET_HEADER_LENGTH);
		}

		public int read() throws IOException {
			int readCount = 0;
			switch (this.readState) {
			case HEADER:
				readCount = ServerConnection.this.socketChannel
						.read(this.readHeaderBuffer);
				if (!this.readHeaderBuffer.hasRemaining()) {
					this.readHeaderBuffer.flip();
					long checksum = this.readHeaderBuffer.getLong();
					short dataLength = this.readHeaderBuffer.getShort();
					this.readDataBuffer = ByteBuffer.allocate(dataLength);
					this.receivedPacketWrapper.setChecksum(checksum);
					this.readHeaderBuffer.clear();
					this.readState = PacketReadState.DATA;
				}
				break;
			case DATA:
				readCount = ServerConnection.this.socketChannel
						.read(this.readDataBuffer);
				if (!this.readDataBuffer.hasRemaining()) {
					this.readDataBuffer.flip();
					this.receivedPacketWrapper.setData(this.readDataBuffer
							.array());
					addReceivedPacket(this.receivedPacketWrapper.checksum,
							this.receivedPacketWrapper.data);
					this.readState = PacketReadState.HEADER;
				}
				break;
			default:
				break;
			}
			return readCount;
		}
	}

	private enum PacketReadState {
		DATA, HEADER;
	}

	private class PacketWriter {
		private Packet lastWritePacket;
		private ByteBuffer writeDataBuffer;

		private void write() throws IOException {
			if (this.lastWritePacket == null) {
				synchronized (sendPackets) {

					this.lastWritePacket = sendPackets.remove();
				}
				this.writeDataBuffer = ByteBuffer
						.allocate(ConnectionProtocol.PACKET_RESPONSE_CODE_SIZE
								+ ConnectionProtocol.PACKET_HEADER_LENGTH
								+ this.lastWritePacket.getDataLength());
				this.writeDataBuffer.put(ResponseCode.OK.getCode());
				this.writeDataBuffer
						.putLong(this.lastWritePacket.getChecksum());
				this.writeDataBuffer.putShort(this.lastWritePacket
						.getDataLength());
				this.writeDataBuffer.put(this.lastWritePacket.getData());
				this.writeDataBuffer.flip();
			}

			ServerConnection.this.socketChannel.write(this.writeDataBuffer);
			if (!this.writeDataBuffer.hasRemaining()) {
				LOGGER.info("Write packet " + this.lastWritePacket.toString()
						+ " out!");
				packetCounter.writeOne();
				this.lastWritePacket = null;
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

	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	/**
	 * Time out interval, in milli-seconds.
	 */
	private static final int TIME_OUT_INTERVAL = 1000 * 60;
	private static AtomicLong UID = new AtomicLong(0);
	private static final short version = 1;

	private ByteBuffer connectHeaderBuffer;
	private ConnectionCode connectionCode;
	private ByteBuffer connectResponseBuffer;
	private ByteBuffer errorResponseBuffer;

	private AtomicBoolean gotErrorPacket;

	private long id;
	private long lastContact;

	private PacketCounter packetCounter;

	private PacketManager packetManager;
	private PacketReader packetReader;

	private PacketWriter packetWriter;

	private byte protocol;

	private boolean readedConnectHeader = false;

	private SelectionKey selectionKey;
	private Queue<Packet> sendPackets;
	private SocketChannel socketChannel;
	private boolean wroteConnectCode = false;

	ServerConnection(SocketChannel clientChannel, PacketManager packetManager,
			SelectionKey selectionKey) {
		this.id = UID.getAndIncrement();

		connectHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.HEADER_LENGTH);
		connectResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.RESPONSE_LENGTH);
		errorResponseBuffer = ByteBuffer
				.allocate(ConnectionProtocol.PACKET_RESPONSE_CODE_SIZE);
		errorResponseBuffer.put(ResponseCode.CRC_ERROR.getCode());
		errorResponseBuffer.flip();
		this.packetManager = packetManager;
		sendPackets = new LinkedList<Packet>();
		lastContact = System.currentTimeMillis();
		packetCounter = new PacketCounter();
		gotErrorPacket = new AtomicBoolean(false);
		this.selectionKey = selectionKey;
		this.socketChannel = clientChannel;
		setConnectionCode(ConnectionCode.OK);
		packetWriter = new PacketWriter();
		packetReader = new PacketReader();
	}

	protected void addReceivedPacket(long checksum, byte[] data)
			throws IOException {
		Packet packet = new Packet(checksum, (short) data.length);
		try {
			packet.setData(data);
		} catch (ChecksumErrorException e) {
			e.printStackTrace();
			gotErrorPacket();
			return;
		}
		LOGGER.info("Read packet:" + packet.toString());
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
		LOGGER.info("add  packet " + packet.toString() + " to send");
	}

	@Override
	public void close() throws IOException {
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

	private void gotErrorPacket() throws IOException {
		gotErrorPacket.set(true);
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
		return System.currentTimeMillis() - lastContact > TIME_OUT_INTERVAL;
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
			return this.packetReader.read();
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
		if (wroteConnectCode) {
			if (shouldWriteErrorResponse())
				writeErrorResponse();
			else
				packetWriter.write();

		} else {
			// write response code.
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