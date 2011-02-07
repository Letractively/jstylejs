package server;

import java.nio.channels.SocketChannel;

import common.AbstractConnection;

class ServerConnection extends AbstractConnection {

	ServerConnection(SocketChannel clientChannel) {
		this.setSocketChannel(clientChannel);
	}

}
