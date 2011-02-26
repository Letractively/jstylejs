package org.rayson.server;

import java.io.IOException;

import org.rayson.transport.server.TransportServerImpl;

class RpcServer extends TransportServerImpl {

	RpcServer(int portNum) {
		super(portNum);
	}

	@Override
	public void start() throws IOException {
		super.start();
		// TODO: start this RPC server .
	}
}
