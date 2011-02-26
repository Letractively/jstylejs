package org.rayson.server;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.rayson.transport.common.Packet;

class CallWorker extends Thread {
	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private RpcServer server;
	private long id;
	private static AtomicLong UID = new AtomicLong(0);

	CallWorker(RpcServer server) {
		this.server = server;
		this.id = UID.getAndIncrement();
		setName("Call worker " + id + " of server " + server.toString());
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void run() {
		LOGGER.info(getName() + " starting ...");

		while (true) {
			try {
				Packet requestPacket = server.getPacketManager().takeReceived();
				// TODO: run call.
			} catch (InterruptedException e) {
				break;
			}
		}

		LOGGER.info(getName() + " stoped");
	}
}
