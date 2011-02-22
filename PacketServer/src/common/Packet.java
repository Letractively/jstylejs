package common;

public class Packet {
	private byte[] data;

	public Packet(byte[] data) {
		if (data == null || data.length == 0)
			throw new IllegalArgumentException();
		if (this.data.length > ConnectionProtocol.MAX_PACKET_DATA_LENGTH)
			throw new IllegalStateException(
					"Data length largar than max packet data length:"
							+ ConnectionProtocol.MAX_PACKET_DATA_LENGTH);
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public short getDataLength() {
		return (short) data.length;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append(", Data length: ");
		sb.append(this.getDataLength());
		sb.append("}");
		return sb.toString();
	}
}