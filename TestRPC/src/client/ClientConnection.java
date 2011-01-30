package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import common.Connection;
import common.PacketWriter;
import common.RpcCall;
import common.RpcPacket;
import common.ThreadSafe;

@ThreadSafe(false)
class ClientConnection implements Connection {

	private SocketAddress serverSocket;
	private SocketChannel channel;

	private ConcurrentLinkedQueue<RpcPacket> requestPackets;

	private RpcPacket currentRequst;

	private PacketWriter packetWriter;

	ClientConnection(SocketAddress serverSocket) {
		this.serverSocket = serverSocket;
		this.requestPackets = new ConcurrentLinkedQueue<RpcPacket>();
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRpcCall(RpcCall call) {
		this.requestPackets.add(call.toPacket());
	}

	@Override
	public boolean isTimeOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read() throws IOException {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void write() throws IOException {
		if (currentRequst == null) {
			currentRequst = requestPackets.remove();
			this.packetWriter.submitPacket(currentRequst);
		}
		if (this.packetWriter.write()) {
			this.currentRequst = null;
		}
	}

	@Override
	public void touch() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws IOException {
		SocketAddress serverSocket = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);
		ClientConnection connection = new ClientConnection(serverSocket);
		connection.build();
		return;
	}

	@Override
	public void build() throws IOException {
		// do connect to remote server.
		this.channel = SocketChannel.open(this.serverSocket);
		this.channel.configureBlocking(false);
		this.packetWriter = new PacketWriter(channel);

	}

}
