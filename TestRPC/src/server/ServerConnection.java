package server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import common.ChecksumNotMatchException;
import common.Connection;
import common.PacketReader;
import common.RpcCall;
import common.RpcPacket;

class ServerConnection implements Connection {

	private SocketChannel clientChannel;

	private PacketReader packetReader;

	ServerConnection(SocketChannel clientChannel) {
		this.clientChannel = clientChannel;
		this.packetReader = new PacketReader(clientChannel);
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRpcCall(RpcCall call) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isTimeOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read() throws IOException {
		RpcPacket packet = null;
		try {

			packet = this.packetReader.read();

		} catch (ChecksumNotMatchException e) {
			e.printStackTrace();
		}

		if (packet != null) {
			System.out.println("Get packet from client: " + packet.toString());
		}
	}

	@Override
	public void touch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void build() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return this.clientChannel.toString();
	}

}
