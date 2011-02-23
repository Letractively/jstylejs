package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Logger;

class PacketServerImpl extends PacketServer {
	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static final int PORT_NUMBER = 4465;

	private static final int version = 1;

	public static void main(String[] args) throws IOException {
		PacketServerImpl server = new PacketServerImpl(PORT_NUMBER);
		server.start();
	}

	private Listener listener;

	PacketServerImpl(int portNum) {
		super(portNum);
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void start() throws IOException {
		this.socketChannel = ServerSocketChannel.open();
		this.socketChannel.configureBlocking(false);
		InetSocketAddress socketAddress = new InetSocketAddress(getPortNumer());
		this.socketChannel.socket().bind(socketAddress);
		this.portNumer = socketAddress.getPort();
		this.listener = new Listener(this);
		this.listener.start();
		LOGGER.info("Server " + this.toString() + " started....");
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("version: ");
		sb.append(version);
		sb.append(", port number: ");
		sb.append(portNumer);
		sb.append("}");
		return sb.toString();
	}
}
