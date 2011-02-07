package server;

import java.io.IOException;

abstract class RpcServer {

	protected int portNumer;

	public abstract void start() throws IOException;

	RpcServer(int portNum) {
		this.portNumer = portNum;
	}

	public int getPortNumer() {
		return portNumer;
	}

}
