package common;

public class Packet {
	private byte[] data;
	private short dataLength;
	private long checksum;
	public static final int HEADER_SIZE = 8 + 2;

	public Packet(long checksum, short dataLength) {
		this.checksum = checksum;
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

	public void setData(byte[] data) throws ChecksumNotMatchException {
		this.data = data;
		this.dataLength = (short) data.length;
	}

	public byte[] getData() {
		return data;
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