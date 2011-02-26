package org.rayson.server;

import org.rayson.transport.server.PacketServerImpl;

class RpcServer extends PacketServerImpl {

	RpcServer(int portNum) {
		super(portNum);
	}
}
