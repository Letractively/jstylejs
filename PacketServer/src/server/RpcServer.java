package server;

import java.io.IOException;

import common.PacketManager;

abstract class RpcServer {

	protected int portNumer;
	private PacketManager packetManager;

	public abstract void start() throws IOException;

	RpcServer(int portNum) {
		this.portNumer = portNum;
		packetManager = new PacketManager();
	}

	public int getPortNumer() {
		return portNumer;
	}

	PacketManager getPacketManager() {
		return packetManager;
	}

}
