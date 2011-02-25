package call.transport.common;

public class PacketCarrier {
	private byte code;
	private Packet packet;

	public PacketCarrier(byte code, Packet packet) {
		this.code = code;
		this.packet = packet;
	}

	public byte getCode() {
		return code;
	}

	public Packet getPacket() {
		return packet;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("code: ");
		sb.append(code);
		sb.append(", packet: ");
		sb.append(packet.toString());
		sb.append("}");
		return sb.toString();
	}
}
