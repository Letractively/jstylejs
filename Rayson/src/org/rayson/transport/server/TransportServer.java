package org.rayson.transport.server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

import org.rayson.transport.server.activity.ActivityConnector;

public abstract class TransportServer {
	public static final int PORT_NUMBER = 4465;
	private ConnectionManager connectionManager;
	private RpcConnector connector;
	private PacketManager packetManager;
	protected int portNumer;
	protected ServerSocketChannel socketChannel;
	private ActivityConnector activityConnector;

	TransportServer(int portNum) {
		this.portNumer = portNum;
		packetManager = new PacketManager();
		connectionManager = new ConnectionManager();
		connector = new RpcConnector(this);
		activityConnector = new ActivityConnector();
	}

	ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public RpcConnector getRpcConnector() {
		return connector;
	}

	ActivityConnector getActivityConnector() {
		return activityConnector;
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

	public abstract int getVersion();

	public abstract void start() throws IOException;
}