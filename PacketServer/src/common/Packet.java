package common;

public class Packet {
	private byte[] data;
	private short dataLength;

	Packet(short dataLength) throws PacketException {
		if (dataLength < 0
				|| dataLength > ConnectionProtocol.MAX_PACKET_DATA_LENGTH)
			throw new PacketException("Wrong packet data length: " + dataLength);
		this.dataLength = dataLength;
	}

	void setData(byte[] data) {
		this.data = data;
	}

	public Packet(byte[] data) throws PacketException {
		if (data == null)
			throw new PacketException("Packet data should not be null");
		if (data.length > ConnectionProtocol.MAX_PACKET_DATA_LENGTH)
			throw new PacketException(
					"Data length largar than max packet data length:"
							+ ConnectionProtocol.MAX_PACKET_DATA_LENGTH);
		this.data = data;
		this.dataLength = (short) data.length;
	}

	public byte[] getData() {
		return data;
	}

	public short getDataLength() {
		return dataLength;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("Data length: ");
		sb.append(dataLength);
		sb.append("}");
		return sb.toString();
	}
}