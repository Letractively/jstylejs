package common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class PacketReader {

	private enum State {
		HEADER, DATA, READY;
	}

	private ByteBuffer readDataBuffer;
	private ByteBuffer readHeaderBuffer;

	private Exception error;

	private State state;
	private SocketChannel socketChannel;

	private Packet lastPacket;

	private byte lastCode;
	private short lastChecksum;

	public PacketReader(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		state = State.HEADER;
		readHeaderBuffer = ByteBuffer
				.allocate(ConnectionProtocol.PACKET_HEADER_LENGTH);

	}

	public int read() throws IOException, ChecksumMatchException,
			PacketException {
		int readCount = 0;
		if (this.error != null)
			return 0;
		switch (this.state) {
		case HEADER:
			readCount = this.socketChannel.read(this.readHeaderBuffer);
			if (!this.readHeaderBuffer.hasRemaining()) {
				this.readHeaderBuffer.flip();
				this.lastCode = this.readHeaderBuffer.get();
				short dataLength = this.readHeaderBuffer.getShort();
				this.lastChecksum = this.readDataBuffer.getShort();
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

	abstract protected void verifyPacket(byte code, short dataLength,
			short checksum, byte[] data) throws ChecksumMatchException,
			PacketException;

	public boolean isReady() {
		return this.state == State.READY;
	}

	public byte getLastCode() {
		return this.lastCode;
	}

	public Packet takeLastPacket() {
		if (!isReady())
			throw new IllegalStateException("Read stat must be ready");
		Packet packet = lastPacket;
		this.state = State.HEADER;
		return packet;

	}
}
