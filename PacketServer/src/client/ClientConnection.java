package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import common.AbstractConnection;
import common.ChecksumNotMatchException;
import common.ConnectionCode;
import common.PacketManager;
import common.RpcPacket;

class ClientConnection extends AbstractConnection {
	private static final short version = 3;
	private static final byte protocol = 1;

	private SocketAddress serverSocket;
	private ByteBuffer writeDataBuffer;
	private Listener listener;

	ClientConnection(SocketAddress serverSocket, PacketManager packetManager,
			Listener listener) {
		super(packetManager);
		this.listener = listener;
		this.serverSocket = serverSocket;
		this.connectHeaderBuffer.put(protocol);
		this.connectHeaderBuffer.putShort(version);
		this.connectHeaderBuffer.clear();
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, ChecksumNotMatchException {
		PacketManager packetManager = new PacketManager();
		SocketAddress serverSocket = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);
		Listener listener = new Listener();
		listener.start();
		ClientConnection connection = new ClientConnection(serverSocket,
				packetManager, listener);
		try {
			connection.init();
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RpcPacket testPacket = new RpcPacket(connection, 233, 324324234,
				(short) 344);
		testPacket.setData(new byte[344]);
		// connection.writeTestPacket(testPacket);
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

	@Override
	public void init() throws IOException, ConnectException {
		// do connect to remote server.
		SocketChannel socketChannel = SocketChannel.open(this.serverSocket);
		this.socketChannel = socketChannel;
		// write connection header to remote server.
		this.socketChannel.write(connectHeaderBuffer);
		// read response
		this.connectHeaderBuffer.clear();
		this.connectHeaderBuffer.limit(1);
		this.socketChannel.read(connectHeaderBuffer);
		connectHeaderBuffer.flip();
		ConnectionCode code = ConnectionCode.valueOf(connectHeaderBuffer.get());
		if (code != ConnectionCode.OK)
			throw new ConnectException(code.name());
		socketChannel.configureBlocking(false);
		this.selectionKey = listener.register(this.socketChannel,
				SelectionKey.OP_READ, this);
		System.out.println(this.toString() + " builded");
	}

	@Override
	public byte getProtocol() {

		return protocol;
	}

	@Override
	public int getVersion() {

		return version;
	}
}
