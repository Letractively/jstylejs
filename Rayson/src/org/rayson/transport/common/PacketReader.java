package org.rayson.transport.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class PacketReader {

	private enum State {
		DATA, HEADER, READY;
	}

	private short lastChecksum;
	private byte lastCode;
	private Packet lastPacket;
	private ByteBuffer readDataBuffer;

	private ByteBuffer readHeaderBuffer;

	private SocketChannel socketChannel;
	private State state;

	public PacketReader(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		state = State.HEADER;
		readHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.PACKET_HEADER_LENGTH);

	}

	public boolean isReady() {
		return this.state == State.READY;
	}

	public int read() throws IOException, ChecksumMatchException,
			PacketException {
		int readCount = 0;

		switch (this.state) {
		case HEADER:
			readCount = this.socketChannel.read(this.readHeaderBuffer);
			if (!this.readHeaderBuffer.hasRemaining()) {
				this.readHeaderBuffer.flip();
				this.lastCode = this.readHeaderBuffer.get();
				short dataLength = this.readHeaderBuffer.getShort();
				this.lastChecksum = this.readHeaderBuffer.getShort();
				this.lastPacket = new Packet(dataLength);
				this.readHeaderBuffer.clear();
				this.readDataBuffer = ByteBuffer.allocate(this.lastPacket
						.getDataLength());
				this.state = State.DATA;
			}
			break;

		case DATA:
			readCount = this.socketChannel.read(this.readDataBuffer);
			if (!this.readDataBuffer.hasRemaining()) {
				this.readDataBuffer.flip();
				byte[] data = this.readDataBuffer.array();
				verifyPacket(this.lastCode, this.lastPacket.getDataLength(),
						this.lastChecksum, data);
				this.lastPacket.setData(data);
				this.state = State.READY;
			}
			break;
		default:
			break;
		}

		return readCount;
	}

	public PacketCarrier takeLastCarrier() {
		if (!isReady())
			throw new IllegalStateException("Read stat must be ready");
		this.state = State.HEADER;
		return new PacketCarrier(lastCode, lastPacket);

	}

	abstract protected void verifyPacket(byte code, short dataLength,
			short checksum, byte[] data) throws ChecksumMatchException,
			PacketException;
}
