package server;

import java.nio.channels.SocketChannel;

import common.AbstractConnection;
import common.RpcCall;

class ServerConnection extends AbstractConnection {

	ServerConnection(SocketChannel clientChannel) {
		this.setSocketChannel(clientChannel);
	}

	@Override
	protected RpcCall readNewCallId(long newCallId) {
		return new ServerCall(newCallId, this);
	}

}
