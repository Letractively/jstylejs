package common;

import java.util.zip.CRC32;

public class Packet {
	private long checksum;
	private byte[] data;

	private static CRC32 crc32 = new CRC32();
	private short dataLength;

	public Packet(long checksum, short dataLength) {
		this.checksum = checksum;
		this.dataLength = dataLength;
	}

	public Packet(byte[] data) {
		if (data == null || data.length == 0)
			throw new IllegalArgumentException();
		this.data = data;
		this.dataLength = (short) data.length;
		this.checksum = computeChecksum(data);
	}

	public long getChecksum() {
		return checksum;
	}

	private static long computeChecksum(byte[] data) {
		synchronized (crc32) {
			crc32.reset();
			crc32.update(data);
			return crc32.getValue();
		}
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
		if (computeChecksum(data) != checksum)
			throw new ChecksumNotMatchException();
		this.data = data;
		this.dataLength = (short) data.length;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("Checksum: ");
		sb.append(this.checksum);
		sb.append(", Data length: ");
		sb.append(this.dataLength);
		sb.append("}");
		return sb.toString();
	}
}