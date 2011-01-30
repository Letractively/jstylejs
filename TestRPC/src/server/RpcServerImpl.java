package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

class RpcServerImpl extends RpcServer {

	public static final int PORT_NUMBER = 4465;

	private ServerSocketChannel socketChannel;

	private Listener listener;

	RpcServerImpl(int portNum) {
		super(portNum);
	}

	@Override
	public void start() throws IOException {

		this.socketChannel = ServerSocketChannel.open();
		this.socketChannel.configureBlocking(false);
		InetSocketAddress socketAddress = new InetSocketAddress(getPortNumer());
		this.socketChannel.socket().bind(socketAddress);
		this.portNumer = socketAddress.getPort();
		this.listener = new Listener(this.socketChannel);
		this.listener.start();
	}

	public static void main(String[] args) throws IOException {
		RpcServerImpl server = new RpcServerImpl(PORT_NUMBER);
		server.start();
	}

}
