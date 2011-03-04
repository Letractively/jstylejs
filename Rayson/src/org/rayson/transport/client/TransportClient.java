package org.rayson.transport.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.exception.NetWorkException;
import org.rayson.transport.common.ConnectionState;
import org.rayson.transport.common.Packet;
import org.rayson.transport.common.PacketException;
import org.rayson.transport.common.ProtocolType;
import org.rayson.transport.common.ResponseType;

public class TransportClient {
	private static TransportClient singleton = new TransportClient();

	public static TransportClient getSingleton() {
		return singleton;
	}

	public static void main(String[] args) throws ConnectException,
			IOException, PacketException {
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);

		RpcConnection connection = TransportClient.getSingleton()
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
		connector = new RpcConnector(this);
		connectionManager = new ConnectionManager();
		packetManager = new PacketManager();
	}

	private void tryLoad() throws IOException {
		synchronized (loaded) {
			if (loaded.compareAndSet(false, true))
				lazyLoad();
		}
	}

	RpcConnection getConnection(SocketAddress serverAddress)
			throws IOException, ConnectException {
		tryLoad();
		RpcConnection connection;
		synchronized (connectionManager) {
			connection = connectionManager.getConnection(serverAddress);
			if (connection != null)
				return connection;
			// Else create new connection and put it to manager.
			connection = new RpcConnection(serverAddress, packetManager,
					listener);
			connection.init();
			connectionManager.accept(connection);
		}
		return connection;
	}

	public RpcConnector getConnector() {
		return connector;
	}

	private void lazyLoad() throws IOException {
		connectionManager.start();
		listener = new Listener(connectionManager);
		listener.start();
	}

	public void notifyConnectionClosed(RpcConnection connection) {
		this.connector.notifyConnectionClosed(connection);
	}

	PacketManager getPacketManager() {
		return packetManager;
	}

	public void ping(SocketAddress serverAddress) throws NetWorkException {
		if (this.connectionManager.getConnection(serverAddress) != null)
			return;
		SocketChannel socketChannel = null;
		// else we should ping remote blocked.
		try {
			socketChannel = SocketChannel.open(serverAddress);
			ByteBuffer pingBuffer = ByteBuffer.allocate(1);
			pingBuffer.put(ProtocolType.PING.getType());
			pingBuffer.flip();
			socketChannel.write(pingBuffer);
			pingBuffer.clear();
			socketChannel.read(pingBuffer);
			pingBuffer.flip();
			ConnectionState connectionState = ConnectionState
					.valueOf(pingBuffer.get());
			switch (connectionState) {
			case SERVICE_UNAVALIABLE:
				throw new NetWorkException(new IOException(
						ConnectionState.SERVICE_UNAVALIABLE.name()));
			case UNKNOWN:

				break;

			default:
				break;
			}
		} catch (IOException e) {
			throw new NetWorkException(e);
		} finally {
			if (socketChannel != null && socketChannel.isOpen()) {
				try {

					socketChannel.close();
				} catch (Throwable e) {
					// ignore it.
				}
			}
		}
	}
}