package common;

public enum ConnectionState {
	;
	private byte code;

	private ConnectionState(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
}
