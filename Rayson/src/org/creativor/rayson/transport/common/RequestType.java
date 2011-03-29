package org.creativor.rayson.transport.common;

public enum RequestType {

	NORMAL((byte) 8), PING((byte) 1), UNKNOWN((byte) 0);

	public static RequestType valueOf(byte type) {
		for (RequestType state : RequestType.values()) {
			if (state.type == type)
				return state;
		}
		return UNKNOWN;
	}

	private byte type;;

	private RequestType(byte type) {
		this.type = type;
	};

	public byte getType() {
		return type;
	};
}
