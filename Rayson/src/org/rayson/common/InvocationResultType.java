package org.rayson.common;

public enum InvocationResultType {

	SUCCESSFUL((byte) 1), EXCEPTION((byte) 0);
	private byte type;

	private InvocationResultType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public static InvocationResultType valueOf(byte type) {
		for (InvocationResultType s : InvocationResultType.values()) {
			if (s.type == type)
				return s;
		}
		return EXCEPTION;
	}
}
