package org.rayson.transport.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.transport.common.Packet;
import org.rayson.transport.common.PacketException;

public class TransportClient {
	private static TransportClient singleton = new TransportClient();

	public static TransportClient getSingleton() {
		return singleton;
	}

	public static void main(String[] args) throws ConnectException,
			IOException, PacketException {
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);

		ClientConnection connection = TransportClient.getSingleton()
				.getConnection(serverAddress);
		byte[] bytes = new byte[344];
		Packet testPacket = new Packet(bytes);
		connection.addSendPacket(testPacket);

	}

	private ConnectionManager connectionManager;
	private RpcConnector connector;
	private Listener listener;
	private AtomicBoolean loaded = new AtomicBoolean(false);
	private PacketManager packetManager;

	private TransportClient() {

	}

	private void tryLoad() throws IOException {
		synchronized (loaded) {
			if (loaded.compareAndSet(false, true))
				lazyLoad();
		}
	}

	ClientConnection getConnection(SocketAddress serverAddress)
			throws IOException, ConnectException {
		tryLoad();
		ClientConnection connection;
		synchronized (connectionManager) {
			connection = connectionManager.getConnection(serverAddress);
			if (connection != null)
				return connection;
			// Else create new connection and put it to manager.
			connection = new ClientConnection(serverAddress, packetManager,
					listener);
			connection.init();
			connectionManager.accept(connection);
		}
		return connection;
	}

	public RpcConnector getConnector() throws IOException {
		tryLoad();
		return connector;
	}

	private void lazyLoad() throws IOException {
		connector = new RpcConnector(this);
		packetManager = new PacketManager();
		connectionManager = new ConnectionManager();
		connectionManager.start();
		listener = new Listener(connectionManager);
		listener.start();
	}

	PacketManager getPacketManager() {
		return packetManager;
	}
}
