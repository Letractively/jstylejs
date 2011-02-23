package common;

public enum ResponseCode {

	CRC_ERROR((byte) 1), OK((byte) 8), UNKNOW_REQUEST_CODE((byte) 5), UNKNOWN(
			(byte) 0);

	public static ResponseCode valueOf(byte code) {
		for (ResponseCode state : ResponseCode.values()) {
			if (state.code == code)
				return state;
		}
		return UNKNOWN;
	}

	private byte code;;

	private ResponseCode(byte code) {
		this.code = code;
	};

	public byte getCode() {
		return code;
	};
}
