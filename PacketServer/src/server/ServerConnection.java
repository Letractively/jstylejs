package server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import common.AbstractConnection;
import common.PacketManager;

class ServerConnection extends AbstractConnection {

	ServerConnection(SocketChannel clientChannel, PacketManager packetManager,
			SelectionKey selectionKey) {
		super(packetManager, selectionKey);
		this.setSocketChannel(clientChannel);
	}

}
