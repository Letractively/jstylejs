package common;

import java.io.DataOutput;
import java.io.IOException;

public abstract class RpcPacket {

	private long callId;
	private byte[] data;
	private short dataLength;
	private long checkSum;

	RpcPacket(long callId, long checkSum, short dataLength) {
		this.callId = callId;
		this.checkSum = checkSum;
		this.dataLength = dataLength;
	}

	public short getDataLength() {
		return dataLength;
	}

	public void setData(byte[] data) throws ChecksumNotMatchException {
		this.data = data;
	}

	RpcPacket(long callId, byte[] data) {
		this.callId = callId;
		this.data = data;
		this.dataLength = (short) data.length;
	}

	public long getCallId() {
		return callId;
	}

	public byte[] getData() {
		return data;
	}

	public abstract Writable readParameter() throws IOException;

	public void write(DataOutput out) throws IOException {
		out.writeLong(this.callId);
		out.writeLong(this.checkSum);
		out.writeShort(this.dataLength);
		out.write(data);
	}
}