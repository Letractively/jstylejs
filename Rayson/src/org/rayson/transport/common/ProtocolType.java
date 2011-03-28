package org.rayson.transport.common;

/**
 * TODO: add protocol type data structure.
 * 
 */
public enum ProtocolType {
	PING((byte) 0), RPC((byte) 1), STREAM((byte) 2), UNKNOWN((byte) 8);
	public static ProtocolType valueOf(byte type) {
		for (ProtocolType t : ProtocolType.values()) {
			if (t.type == type)
				return t;
		}

		return UNKNOWN;
	}

	private byte type;;

	private ProtocolType(byte type) {
		this.type = type;
	};

	public byte getType() {
		return type;
	};
}
