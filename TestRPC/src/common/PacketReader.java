package common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@ThreadSafe(false)
class PacketReader {

	private PacketOperation operation;
	private ByteBuffer longByteBuffer;
	private SocketChannel socketChannel;

	private RpcPacket lastPaket;

	private ByteBuffer lastDataBuffer;

	public PacketReader(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		operation = PacketOperation.CALL_ID;
		this.longByteBuffer = ByteBuffer.allocate(8);
	}

	public Long readNewCall() throws IOException {
		if (this.operation != PacketOperation.CALL_ID)
			throw new IllegalStateException();
		if (this.longByteBuffer.hasRemaining())
			this.socketChannel.read(longByteBuffer);
		if (!this.longByteBuffer.hasRemaining()) {
			this.longByteBuffer.flip();
			long callId = this.longByteBuffer.getLong();
			operation = PacketOperation.CHECKSUM;
			return callId;
		}
		return null;
	}

	public PacketOperation getOperation() {
		return operation;
	}

	public RpcPacket read() throws IOException, ChecksumNotMatchException {

		if (this.operation == PacketOperation.CALL_ID)
			throw new IllegalStateException();
		switch (operation) {

		case CHECKSUM:

			if (this.longByteBuffer.hasRemaining())
				this.socketChannel.read(longByteBuffer);
			if (!this.longByteBuffer.hasRemaining()) {
				this.longByteBuffer.flip();
				long checksum = this.longByteBuffer.getLong();
				this.lastPaket.setChecksum(checksum);
				this.longByteBuffer.clear();
				this.longByteBuffer.limit(4);
				operation = PacketOperation.DATA_LENGTH;
			}
			return null;
		case DATA_LENGTH:
			if (this.longByteBuffer.hasRemaining())
				this.socketChannel.read(longByteBuffer);
			if (!this.longByteBuffer.hasRemaining()) {
				this.longByteBuffer.flip();
				short lastDataLength = this.longByteBuffer.getShort();
				this.lastDataBuffer = ByteBuffer.allocate(lastDataLength);
				this.longByteBuffer.clear();
				operation = PacketOperation.DATA;
			}
			return null;

		case DATA:
			if (this.lastDataBuffer.hasRemaining())
				this.socketChannel.read(lastDataBuffer);
			if (!this.lastDataBuffer.hasRemaining()) {
				this.lastDataBuffer.flip();
				this.lastPaket.setData(this.lastDataBuffer.array());
				operation = PacketOperation.CALL_ID;
				return this.lastPaket;
			}
			return null;

		default:
			break;
		}
		return null;
	}

	public void submitLastPacket(RpcPacket requestPacket) {
		this.lastPaket = requestPacket;
	}
}
