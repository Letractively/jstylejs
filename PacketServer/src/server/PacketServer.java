package server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

import common.PacketManager;

abstract class PacketServer {

	protected int portNumer;
	private PacketManager packetManager;
	private ConnectionManager connectionManager;
	protected ServerSocketChannel socketChannel;

	public abstract void start() throws IOException;

	PacketServer(int portNum) {
		this.portNumer = portNum;
		packetManager = new PacketManager();
		connectionManager = new ConnectionManager();
	}

	public int getPortNumer() {
		return portNumer;
	}

	PacketManager getPacketManager() {
		return packetManager;
	}

	ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	ServerSocketChannel getSocketChannel() {
		return socketChannel;
	}
}