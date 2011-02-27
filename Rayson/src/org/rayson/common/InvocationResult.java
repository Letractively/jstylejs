package org.rayson.common;

public enum InvocationResult {

	SUCCESSFUL((byte) 1), EXCEPTION((byte) 0);
	private byte type;

	private InvocationResult(byte type) {
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public static InvocationResult valueOf(byte type) {
		for (InvocationResult s : InvocationResult.values()) {
			if (s.type == type)
				return s;
		}
		return EXCEPTION;
	}
}
