/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.common;

public enum ResponseType {

	CRC_ERROR((byte) 1), OK((byte) 8), UNKNOW_REQUEST_Type((byte) 5), UNKNOWN(
			(byte) 0);

	public static ResponseType valueOf(byte type) {
		for (ResponseType state : ResponseType.values()) {
			if (state.type == type)
				return state;
		}
		return UNKNOWN;
	}

	private byte type;;

	private ResponseType(byte type) {
		this.type = type;
	};

	public byte getType() {
		return type;
	};
}
