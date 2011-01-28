package common;

public abstract class RpcCall {
	private long id;

	RpcCall(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public abstract RpcPacket toPacket();
}
