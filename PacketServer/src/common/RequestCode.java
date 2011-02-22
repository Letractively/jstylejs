package common;

public enum RequestCode {

	PING((byte) 1), NORMAL((byte) 8), UNKNOWN((byte) 0);

	public static RequestCode valueOf(byte code) {
		for (RequestCode state : RequestCode.values()) {
			if (state.code == code)
				return state;
		}
		return UNKNOWN;
	}

	private byte code;;

	private RequestCode(byte code) {
		this.code = code;
	};

	public byte getCode() {
		return code;
	};
}
