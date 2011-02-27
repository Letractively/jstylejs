package org.rayson.common;

public enum ResponseState {

	SUCCESSFUL((byte) 1), EXCEPTION((byte) 0);
	private byte state;

	private ResponseState(byte state) {
		this.state = state;
	}

	public byte getState() {
		return state;
	}

	public static ResponseState valueOf(byte state) {
		for (ResponseState s : ResponseState.values()) {
			if (s.state == state)
				return s;
		}
		return EXCEPTION;
	}
}
