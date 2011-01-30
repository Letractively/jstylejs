package client;

import common.RpcCall;

class ClientCall extends RpcCall {

	protected ClientCall(long id, ClientConnection connection) {
		super(id, connection);
	}

}
