package org.rayson.transport.common;

public class PacketWithType {
	private byte type;
	private Packet packet;

	public PacketWithType(byte type, Packet packet) {
		this.type = type;
		this.packet = packet;
	}

	public byte getType() {
		return type;
	}

	public Packet getPacket() {
		return packet;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("type: ");
		sb.append(type);
		sb.append(", packet: ");
		sb.append(packet.toString());
		sb.append("}");
		return sb.toString();
	}
}
