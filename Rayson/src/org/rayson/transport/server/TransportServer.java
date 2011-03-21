package org.rayson.transport.server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;

import org.rayson.transport.server.activity.ActivityInvoker;

public abstract class TransportServer {
	public static final int PORT_NUMBER = 4465;
	private ConnectionManager connectionManager;
	private RpcConnector connector;
	private PacketManager packetManager;
	protected int portNumer;
	protected ServerSocketChannel socketChannel;

	@SuppressWarnings("unused")
	private HashMap<Short, ActivityInvoker> activityInvokers;

	TransportServer(int portNum) {
		this.portNumer = portNum;
		activityInvokers = new HashMap<Short, ActivityInvoker>();
		packetManager = new PacketManager();
		connectionManager = new ConnectionManager();
		connector = new RpcConnector(this);
	}

	ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public RpcConnector getConnector() {
		return connector;
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