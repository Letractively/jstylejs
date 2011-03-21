package org.rayson.transport.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Logger;

import org.rayson.util.Log;

public class TransportServerImpl extends TransportServer {
	private static Logger LOGGER = Log.getLogger();
	private static final int version = 1;

	public static void main(String[] args) throws IOException {
		TransportServerImpl server = new TransportServerImpl(PORT_NUMBER);
		server.start();
	}

	private Listener listener;

	protected TransportServerImpl(int portNum) {
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
		this.getConnectionManager().start();
		this.getActivityConnector().start();
		LOGGER.info("Server " + this.toString() + " started....");
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("version: ");
		sb.append(version);
		sb.append(", port: ");
		sb.append(portNumer);
		sb.append("}");
		return sb.toString();
	}
}
