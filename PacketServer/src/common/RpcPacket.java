package common;

import java.io.DataOutput;
import java.io.IOException;

public class RpcPacket {

	private byte[] data;
	private short dataLength;
	private long checksum;
	private long callId;
	private Connection connection;

	RpcPacket(Connection connection, long callId, long checkSum,
			short dataLength) {
		this.connection = connection;
		this.callId = callId;
		this.checksum = checkSum;
		this.dataLength = dataLength;
	}

	public short getDataLength() {
		return dataLength;
	}

	public long getChecksum() {
		return checksum;
	}

	void setChecksum(long checkSum) {
		this.checksum = checkSum;
	}

	void setData(byte[] data) throws ChecksumNotMatchException {
		this.data = data;
		this.dataLength = (short) data.length;
	}

	public long getCallId() {
		return callId;
	}

	public byte[] getData() {
		return data;
	}

	public void write(DataOutput out) throws IOException {
		out.writeLong(this.callId);
		out.writeLong(this.checksum);
		out.writeShort(this.dataLength);
		out.write(data);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("Call id: ");
		sb.append(this.callId);
		sb.append(", Checksum: ");
		sb.append(this.checksum);
		sb.append(", Data length: ");
		sb.append(this.dataLength);
		sb.append("}");
		return sb.toString();
	}
}