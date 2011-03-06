package org.rayson.common;

public enum InvocationResultType {

	EXCEPTION((byte) 0), SUCCESSFUL((byte) 1);
	public static InvocationResultType valueOf(byte type) {
		for (InvocationResultType s : InvocationResultType.values()) {
			if (s.type == type)
				return s;
		}
		return EXCEPTION;
	}

	private byte type;

	private InvocationResultType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return type;
	}
}
