package common;

import java.io.DataOutput;
import java.io.IOException;

public class RpcPacket {

	private byte[] data;
	private short dataLength;
	private long checksum;
	private RpcCall call;

	RpcPacket(RpcCall call, long checkSum, short dataLength) {
		this.call = call;
		this.checksum = checkSum;
		this.dataLength = dataLength;
	}

	public short getDataLength() {
		return dataLength;
	}

	public long getChecksum() {
		return checksum;
	}

	RpcPacket(RpcCall call) {
		this.call = call;
	}

	void setChecksum(long checkSum) {
		this.checksum = checkSum;
	}

	void setData(byte[] data) throws ChecksumNotMatchException {
		this.data = data;
		this.dataLength = (short) data.length;
	}

	public RpcCall getCall() {
		return call;
	}

	public byte[] getData() {
		return data;
	}

	public void write(DataOutput out) throws IOException {
		out.writeLong(this.call.getId());
		out.writeLong(this.checksum);
		out.writeShort(this.dataLength);
		out.write(data);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("Call id: ");
		sb.append(this.call.getId());
		sb.append(", Checksum: ");
		sb.append(this.checksum);
		sb.append(", Data length: ");
		sb.append(this.dataLength);
		sb.append("}");
		return sb.toString();
	}
}