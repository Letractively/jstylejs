package org.rayson.transport.server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

import org.rayson.api.TransferService;
import org.rayson.exception.IllegalServiceException;
import org.rayson.transport.api.ServiceAlreadyExistedException;
import org.rayson.transport.server.transfer.TransferConnector;

public abstract class TransportServer {
	public static final int PORT_NUMBER = 4465;
	private ConnectionManager connectionManager;
	private RpcConnector connector;
	private PacketManager packetManager;
	protected int portNumer;
	protected ServerSocketChannel socketChannel;
	private TransferConnector transferConnector;

	TransportServer() {
		int portNumber = PORT_NUMBER;
		ServerConfig config = this.getClass().getAnnotation(ServerConfig.class);
		if (config != null && config.portNumber() > 0)
			portNumber = config.portNumber();
		this.portNumer = portNumber;
		packetManager = new PacketManager();
		connectionManager = new ConnectionManager();
		connector = new RpcConnector(this);
		transferConnector = new TransferConnector();
	}

	ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public RpcConnector getRpcConnector() {
		return connector;
	}

	TransferConnector getTransferConnector() {
		return transferConnector;
	}

	PacketManager getPacketManager() {
		return packetManager;
	}

	public int getPortNumer() {
		return portNumer;
	}

	public void registerService(TransferService service)
			throws ServiceAlreadyExistedException, IllegalServiceException {
		this.transferConnector.registerService(service);
	}

	ServerSocketChannel getSocketChannel() {
		return socketChannel;
	}

	public abstract int getVersion();

	public abstract void start() throws IOException;
}