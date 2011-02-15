package common;

public enum ResponseCode {
	OK((byte) 8), CRC_ERROR((byte) 1), EXECEPTION((byte) 3), UNKNOWN((byte) 0);
	private byte code;

	private ResponseCode(byte code) {
		this.code = code;
	};

	public byte getCode() {
		return code;
	};

	public static ResponseCode valueOf(byte code) {
		for (ResponseCode state : ResponseCode.values()) {
			if (state.code == code)
				return state;
		}
		return UNKNOWN;
	};
}
