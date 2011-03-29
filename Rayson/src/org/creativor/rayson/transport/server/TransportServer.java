package org.creativor.rayson.transport.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Logger;

import org.creativor.rayson.api.TransferService;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.transport.api.ServiceAlreadyExistedException;
import org.creativor.rayson.transport.server.transfer.TransferConnector;
import org.creativor.rayson.util.Log;

@ServerConfig()
public class TransportServer {
	private static Logger LOGGER = Log.getLogger();
	private static final int version = 1;
	private ServerConfig config;
	private ConnectionManager connectionManager;
	private RpcConnector connector;
	private Listener listener;
	private PacketManager packetManager;

	protected int portNumber;
	protected ServerSocketChannel socketChannel;

	private TransferConnector transferConnector;

	public TransportServer() {
		config = this.getClass().getAnnotation(ServerConfig.class);
		this.portNumber = config.portNumber();
		packetManager = new PacketManager();
		connectionManager = new ConnectionManager();
		connector = new RpcConnector(this);
		transferConnector = new TransferConnector();
	}

	public TransportServer(short portNumber) {
		this();
		this.portNumber = portNumber;
	}

	protected ServerConfig getConfig() {
		return config;
	}

	ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	PacketManager getPacketManager() {
		return packetManager;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public RpcConnector getRpcConnector() {
		return connector;
	}

	ServerSocketChannel getSocketChannel() {
		return socketChannel;
	}

	TransferConnector getTransferConnector() {
		return transferConnector;
	}

	public int getVersion() {
		return version;
	}

	public void registerService(TransferService service)
			throws ServiceAlreadyExistedException, IllegalServiceException {
		this.transferConnector.registerService(service);
	}

	public void start() throws IOException {
		this.socketChannel = ServerSocketChannel.open();
		this.socketChannel.configureBlocking(false);
		InetSocketAddress socketAddress = new InetSocketAddress(getPortNumber());
		this.socketChannel.socket().bind(socketAddress);
		this.portNumber = socketAddress.getPort();
		this.listener = new Listener(this);
		this.listener.start();
		this.getConnectionManager().start();
		this.getTransferConnector().start();
		LOGGER.info("Server " + this.toString() + " started....");
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("version: ");
		sb.append(version);
		sb.append(", port: ");
		sb.append(portNumber);
		sb.append("}");
		return sb.toString();
	}

}