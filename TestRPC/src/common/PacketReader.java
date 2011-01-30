package common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@ThreadSafe(false)
public class PacketReader {
	private enum ReadStep {
		CALL_ID, CHECKSUM, DATA_LENGTH, DATA
	}

	private ReadStep step;
	private ByteBuffer longByteBuffer;
	private Connection connection;

	private RpcPacket currentPacket;

	private ByteBuffer lastDataBuffer;

	public PacketReader(Connection connection) {
		this.connection = connection;
		step = ReadStep.CALL_ID;
		this.longByteBuffer = ByteBuffer.allocate(8);
	}

	public RpcPacket read() throws IOException, ChecksumNotMatchException {

		switch (step) {
		case CALL_ID:

			if (this.longByteBuffer.hasRemaining())
				this.connection.readData(this.longByteBuffer);
			if (!this.longByteBuffer.hasRemaining()) {
				this.longByteBuffer.flip();
				long callId = this.longByteBuffer.getLong();
				
				this.currentPacket = new RpcPacket(callId);
				this.longByteBuffer.clear();
				step = ReadStep.CHECKSUM;
			}
			return null;

		case CHECKSUM:

			if (this.longByteBuffer.hasRemaining())
				this.clientChannel.read(longByteBuffer);
			if (!this.longByteBuffer.hasRemaining()) {
				this.longByteBuffer.flip();
				long checksum = this.longByteBuffer.getLong();
				this.currentPacket.setChecksum(checksum);
				this.longByteBuffer.clear();
				this.longByteBuffer.limit(4);
				step = ReadStep.DATA_LENGTH;
			}
			return null;
		case DATA_LENGTH:
			if (this.longByteBuffer.hasRemaining())
				this.clientChannel.read(longByteBuffer);
			if (!this.longByteBuffer.hasRemaining()) {
				this.longByteBuffer.flip();
				short lastDataLength = this.longByteBuffer.getShort();
				this.lastDataBuffer = ByteBuffer.allocate(lastDataLength);
				this.longByteBuffer.clear();
				step = ReadStep.DATA;
			}
			return null;

		case DATA:
			if (this.lastDataBuffer.hasRemaining())
				this.clientChannel.read(lastDataBuffer);
			if (!this.lastDataBuffer.hasRemaining()) {
				this.lastDataBuffer.flip();
				this.currentPacket.setData(this.lastDataBuffer.array());
				step = ReadStep.CALL_ID;
				return this.currentPacket;
			}
			return null;

		default:
			break;
		}
		return null;
	}
}
