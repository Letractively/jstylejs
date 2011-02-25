package call.transport.common;

public enum ConnectionCode {
	OK((byte) 8), SERVICE_UNAVALIABLE((byte) 2), UNKNOWN((byte) 0), WRONG_VERSION(
			(byte) 1);
	public static ConnectionCode valueOf(byte code) {
		for (ConnectionCode state : ConnectionCode.values()) {
			if (state.code == code)
				return state;
		}
		return UNKNOWN;
	}

	private byte code;;

	private ConnectionCode(byte code) {
		this.code = code;
	};

	public byte getCode() {
		return code;
	};
}
