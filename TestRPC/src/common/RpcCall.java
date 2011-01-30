package common;

public abstract class RpcCall {

	private long id;

	private RpcPacket responsePacket;

	private RpcPacket requestPacket;

	private Connection connection;

	protected RpcCall(long id, Connection connection) {
		this.connection = connection;
		this.id = id;
		this.requestPacket = new RpcPacket(this);
		this.responsePacket = new RpcPacket(this);
	}

	public Connection getConnection() {
		return connection;
	}

	public long getId() {
		return id;
	}

	public RpcPacket getRequestPacket() {
		return requestPacket;
	}

	public RpcPacket getResponsePacket() {
		return responsePacket;
	}

}
