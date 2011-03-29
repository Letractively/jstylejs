package org.creativor.rayson.transport.common;

public enum ConnectionState {
	OK((byte) 8), SERVICE_UNAVALIABLE((byte) 2), UNKNOWN((byte) 0), UNSUPPORTED_VERSION(
			(byte) 1);
	public static ConnectionState valueOf(byte state) {
		for (ConnectionState stat : ConnectionState.values()) {
			if (stat.state == state)
				return stat;
		}
		return UNKNOWN;
	}

	private byte state;;

	private ConnectionState(byte state) {
		this.state = state;
	};

	public byte getState() {
		return state;
	};
}
