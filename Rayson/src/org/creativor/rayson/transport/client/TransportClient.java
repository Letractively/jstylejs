/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

import org.creativor.rayson.api.TransferArgument;
import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.NetWorkException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.exception.UnsupportedVersionException;
import org.creativor.rayson.transport.common.ConnectionState;
import org.creativor.rayson.transport.common.Packet;
import org.creativor.rayson.transport.common.PacketException;
import org.creativor.rayson.transport.common.ProtocolType;

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

	PacketManager getPacketManager() {
		return packetManager;
	}

	private void lazyLoad() throws IOException {
		connectionManager.start();
		listener = new Listener(connectionManager);
		listener.start();
	}

	public void notifyConnectionClosed(RpcConnection connection) {
		this.connector.notifyConnectionClosed(connection);
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

	private void tryLoad() throws IOException {
		synchronized (loaded) {
			if (loaded.compareAndSet(false, true))
				lazyLoad();
		}
	}

	public TransferSocket createTransferSocket(SocketAddress serverAddress,
			TransferArgument argument) throws IOException, ConnectException,
			ServiceNotFoundException, IllegalServiceException,
			UnsupportedVersionException {
		ClientStreamConnection connection = new ClientStreamConnection(
				serverAddress, argument, connectionManager);
		connection.init();
		connectionManager.accept(connection);
		try {
			return connection.createTransferSocket();
		} catch (IOException e) {
			connectionManager.remove(connection);
			throw e;
		}
	}
}