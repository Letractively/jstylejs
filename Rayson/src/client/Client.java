package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import common.Packet;
import common.PacketException;
import common.PacketManager;

final class Client {

	private static ConnectionManager connectionManager;
	private static Listener listener;
	private static AtomicBoolean loaded = new AtomicBoolean(false);
	private static PacketManager packetManager;

	public static ClientConnection getConnection(SocketAddress serverAddress)
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

	private static void lazyLoad() throws IOException {
		packetManager = new PacketManager();
		connectionManager = new ConnectionManager();
		connectionManager.start();
		listener = new Listener(connectionManager);
		listener.start();
	}

	public static void main(String[] args) throws ConnectException,
			IOException, PacketException {
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);

		ClientConnection connection = Client.getConnection(serverAddress);
		byte[] bytes = new byte[344];
		Packet testPacket = new Packet(bytes);
		connection.addSendPacket(testPacket);
	}
}
