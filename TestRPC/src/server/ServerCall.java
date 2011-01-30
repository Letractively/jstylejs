package server;

import common.RpcCall;

class ServerCall extends RpcCall {

	ServerCall(long id, ServerConnection connection) {
		super(id, connection);
	}

}
