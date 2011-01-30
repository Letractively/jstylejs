package common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@ThreadSafe(false)
class PacketWriter {

	private SocketChannel socketChannel;

	private ByteBuffer longByteBuffer;

	private ByteBuffer dataByteBuffer;
	private PacketOperation operation;

	private RpcPacket lastPacket;

	public PacketWriter(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		this.operation = PacketOperation.CALL_ID;
		longByteBuffer = ByteBuffer.allocate(8);
	}

	public void submitPacket(RpcPacket packet) {
		this.operation = PacketOperation.CALL_ID;
		this.lastPacket = packet;
		this.longByteBuffer.clear();
		this.longByteBuffer.putLong(packet.getCall().getId());
		this.dataByteBuffer = ByteBuffer.allocate(packet.getDataLength());
		this.dataByteBuffer.put(packet.getData());
		this.dataByteBuffer.flip();
		this.longByteBuffer.flip();
	}

	public boolean write() throws IOException {
		switch (operation) {
		case CALL_ID:
			if (this.longByteBuffer.hasRemaining())
				this.socketChannel.write(longByteBuffer);
			if (!this.longByteBuffer.hasRemaining()) {
				this.longByteBuffer.clear();
				this.longByteBuffer.putLong(lastPacket.getChecksum());
				this.longByteBuffer.flip();
				operation = PacketOperation.CHECKSUM;
			}
			return false;

		case CHECKSUM:
			if (this.longByteBuffer.hasRemaining())
				this.socketChannel.write(longByteBuffer);
			if (!this.longByteBuffer.hasRemaining()) {
				this.longByteBuffer.clear();
				this.longByteBuffer.limit(4);
				short dataLength = lastPacket.getDataLength();
				this.longByteBuffer.putShort(dataLength);
				this.longByteBuffer.flip();
				operation = PacketOperation.DATA_LENGTH;
			}
			return false;

		case DATA_LENGTH:

			if (this.longByteBuffer.hasRemaining())
				this.socketChannel.write(longByteBuffer);
			if (!this.longByteBuffer.hasRemaining()) {
				this.longByteBuffer.clear();
				operation = PacketOperation.DATA;
			}
			return false;

		case DATA:

			if (this.dataByteBuffer.hasRemaining())
				this.socketChannel.write(dataByteBuffer);
			if (!this.dataByteBuffer.hasRemaining()) {
				this.dataByteBuffer.clear();
				operation = PacketOperation.CALL_ID;
				return true;
			}
			return false;

		default:
			break;
		}
		return false;
	}
}
