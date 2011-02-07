package server;

import java.nio.channels.SocketChannel;

import common.AbstractConnection;
import common.PacketManager;

class ServerConnection extends AbstractConnection {

	ServerConnection(SocketChannel clientChannel, PacketManager packetManager) {
		super(packetManager);
		this.setSocketChannel(clientChannel);
	}

}
