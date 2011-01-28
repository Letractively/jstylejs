package server;

import java.io.IOException;

abstract class RpcServer {

	private int portNumer;

	public abstract void start() throws IOException;

	RpcServer(int portNum) {
		this.portNumer = portNum;
	}

	public int getPortNumer() {
		return portNumer;
	}
}
