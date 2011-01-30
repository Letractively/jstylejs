package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import common.AbstractConnection;
import common.RpcCall;
import common.ThreadSafe;

@ThreadSafe(false)
class ClientConnection extends AbstractConnection {

	private SocketAddress serverSocket;

	ClientConnection(SocketAddress serverSocket) {
		super();
		this.serverSocket = serverSocket;
	}

	public static void main(String[] args) throws IOException {
		SocketAddress serverSocket = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);
		ClientConnection connection = new ClientConnection(serverSocket);
		connection.build();
		return;
	}

	public void build() throws IOException {
		// do connect to remote server.
		SocketChannel socketChannel = SocketChannel.open(this.serverSocket);
		socketChannel.configureBlocking(false);
		this.setSocketChannel(socketChannel);

	}

	@Override
	protected RpcCall readNewCallId(long newCallId) {
		return new ClientCall(newCallId, this);
	}

}
