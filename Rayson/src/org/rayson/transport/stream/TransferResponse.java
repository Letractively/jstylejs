package org.rayson.transport.stream;

public enum TransferResponse {
	OK((byte) 1), NO_ACTIVITY_FOUND((byte) 2), UNKNOWN((byte) -1);
	private TransferResponse(byte code) {
		this.code = code;
	}

	private byte code;

	public byte getCode() {
		return this.code;
	}

	public static TransferResponse valueOf(byte code) {
		switch (code) {
		case 1:
			return OK;
		case 2:
			return NO_ACTIVITY_FOUND;
		default:
			return UNKNOWN;
		}
	}
}