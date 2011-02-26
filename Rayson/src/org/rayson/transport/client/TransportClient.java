package org.rayson.transport.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.transport.common.Packet;
import org.rayson.transport.common.PacketException;
import org.rayson.transport.common.PacketManager;

public class TransportClient {
	private static TransportClient instance = new TransportClient();

	private TransportClient() {

	}

	public static TransportClient getInstance() {
		return instance;
	}

	private TransportConnector connector;
	private ConnectionManager connectionManager;
	private Listener listener;
	private AtomicBoolean loaded = new AtomicBoolean(false);
	private PacketManager packetManager;

	ClientConnection getConnection(SocketAddress serverAddress)
			throws IOException, ConnectException {

		synchronized (loaded) {
			if (loaded.compareAndSet(false, true))
				lazyLoad();
		}
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

	private void lazyLoad() throws IOException {
		connector = new TransportConnector(this);
		packetManager = new PacketManager();
		connectionManager = new ConnectionManager();
		connectionManager.start();
		listener = new Listener(connectionManager);
		listener.start();
	}

	public TransportConnector getConnector() {
		return connector;
	}

	public static void main(String[] args) throws ConnectException,
			IOException, PacketException {
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);

		ClientConnection connection = TransportClient.getInstance()
				.getConnection(serverAddress);
		byte[] bytes = new byte[344];
		Packet testPacket = new Packet(bytes);
		connection.addReqeustPacket(testPacket);
	}

	void submitCall(SocketAddress serverAddress, Packet requestPacket)
			throws ConnectException, IOException {
		ClientConnection connection = getConnection(serverAddress);
		connection.addReqeustPacket(requestPacket);
	}
}
