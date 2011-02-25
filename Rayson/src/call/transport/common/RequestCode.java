package call.transport.common;

public enum RequestCode {

	NORMAL((byte) 8), PING((byte) 1), UNKNOWN((byte) 0);

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
