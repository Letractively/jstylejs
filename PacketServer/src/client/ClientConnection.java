package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import common.AbstractConnection;
import common.ChecksumNotMatchException;
import common.PacketManager;
import common.RpcPacket;

class ClientConnection extends AbstractConnection {

	private SocketAddress serverSocket;
	private ByteBuffer writeDataBuffer;

	ClientConnection(SocketAddress serverSocket, PacketManager packetManager) {
		super(packetManager, null);
		this.serverSocket = serverSocket;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, ChecksumNotMatchException {
		PacketManager packetManager = new PacketManager();
		SocketAddress serverSocket = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);
		Selector selector = Selector.open();
		Listener listener = new Listener(selector);
		listener.start();
		ClientConnection connection = new ClientConnection(serverSocket,
				packetManager);
		connection.build(listener);
		RpcPacket testPacket = new RpcPacket(connection, 233, 324324234,
				(short) 344);
		testPacket.setData(new byte[344]);
		connection.writeTestPacket(testPacket);
		Thread.sleep(100000);
		return;
	}

	private void writeTestPacket(RpcPacket rpcPacket) throws IOException {
		this.writeDataBuffer = ByteBuffer.allocate(rpcPacket.getDataLength()
				+ RpcPacket.HEADER_SIZE);
		this.writeDataBuffer.putLong(rpcPacket.getCallId());
		this.writeDataBuffer.putLong(rpcPacket.getChecksum());
		this.writeDataBuffer.putShort(rpcPacket.getDataLength());
		this.writeDataBuffer.put(rpcPacket.getData());
		this.writeDataBuffer.flip();
		while (this.writeDataBuffer.hasRemaining()) {
			this.getSocketChannel().write(writeDataBuffer);
		}
		System.out.println("Write packet out:" + rpcPacket.toString());
	}

	public void build(Listener listener) throws IOException {
		// do connect to remote server.
		SocketChannel socketChannel = SocketChannel.open(this.serverSocket);
		socketChannel.configureBlocking(false);
		this.socketChannel = socketChannel;
		this.selectionKey = listener.register(this.socketChannel,
				SelectionKey.OP_READ, this);
		System.out.println(this.toString() + " builded");
	}
}
