package common;

public class Packet {
	private long checksum;
	private byte[] data;
	private short dataLength;

	public Packet(long checksum, short dataLength) {
		this.checksum = checksum;
		this.dataLength = dataLength;
	}

	public long getChecksum() {
		return checksum;
	}

	public byte[] getData() {
		return data;
	}

	public short getDataLength() {
		return dataLength;
	}

	void setChecksum(long checkSum) {
		this.checksum = checkSum;
	}

	public void setData(byte[] data) throws ChecksumNotMatchException {
		this.data = data;
		this.dataLength = (short) data.length;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append(", Checksum: ");
		sb.append(this.checksum);
		sb.append(", Data length: ");
		sb.append(this.dataLength);
		sb.append("}");
		return sb.toString();
	}
}