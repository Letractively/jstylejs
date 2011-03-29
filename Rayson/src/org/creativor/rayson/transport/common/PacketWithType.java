package org.creativor.rayson.transport.common;

public class PacketWithType {
	private Packet packet;
	private byte type;

	public PacketWithType(byte type, Packet packet) {
		this.type = type;
		this.packet = packet;
	}

	public Packet getPacket() {
		return packet;
	}

	public byte getType() {
		return type;
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
