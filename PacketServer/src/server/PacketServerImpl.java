package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Logger;

class PacketServerImpl extends PacketServer {
	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static final int PORT_NUMBER = 4465;

	public static void main(String[] args) throws IOException {
		PacketServerImpl server = new PacketServerImpl(PORT_NUMBER);
		server.start();
	}

	private Listener listener;

	PacketServerImpl(int portNum) {
		super(portNum);
	}

	@Override
	public void start() throws IOException {
		LOGGER.info("Server starting....");
		this.socketChannel = ServerSocketChannel.open();
		this.socketChannel.configureBlocking(false);
		InetSocketAddress socketAddress = new InetSocketAddress(getPortNumer());
		this.socketChannel.socket().bind(socketAddress);
		this.portNumer = socketAddress.getPort();
		this.listener = new Listener(this);
		this.listener.start();
	}

}
