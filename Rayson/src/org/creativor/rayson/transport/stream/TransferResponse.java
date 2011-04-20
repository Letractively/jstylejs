/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.stream;

public enum TransferResponse {
	NO_ACTIVITY_FOUND((byte) 2), OK((byte) 1), UNKNOWN((byte) -1), ARGUMENT_ERROR(
			(byte) 3), UNSUPPORTED_VERSION((byte) 4);
	public static TransferResponse valueOf(byte code) {
		switch (code) {
		case 1:
			return OK;
		case 2:
			return NO_ACTIVITY_FOUND;
		case 3:
			return ARGUMENT_ERROR;
		case 4:
			return UNSUPPORTED_VERSION;
		default:
			return UNKNOWN;
		}
	}

	private byte code;

	private TransferResponse(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return this.code;
	}
}