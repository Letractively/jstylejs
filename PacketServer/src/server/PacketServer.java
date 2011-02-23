package server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

import common.PacketManager;

abstract class PacketServer {

	private ConnectionManager connectionManager;
	private PacketManager packetManager;
	protected int portNumer;
	protected ServerSocketChannel socketChannel;

	PacketServer(int portNum) {
		this.portNumer = portNum;
		packetManager = new PacketManager();
		connectionManager = new ConnectionManager();
	}

	ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	PacketManager getPacketManager() {
		return packetManager;
	}

	public int getPortNumer() {
		return portNumer;
	}

	ServerSocketChannel getSocketChannel() {
		return socketChannel;
	}

	public abstract void start() throws IOException;

	public abstract int getVersion();
}