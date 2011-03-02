package org.rayson.server;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.rayson.transport.common.PacketException;
import org.rayson.util.Log;

class CallWorker extends Thread {
	private static Logger LOGGER = Log.getLogger();
	private static AtomicLong UID = new AtomicLong(0);
	private long id;
	private RpcServer server;

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
				ServerCall call = this.server.getConnector().takeCall();
				this.server.invokeCall(call);
				try {
					try {
						this.server.getConnector().responseCall(call.getId(),
								call.getResponsePacket());
					} catch (PacketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO: handling this exception
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				break;
			}
		}

		LOGGER.info(getName() + " stoped");
	}
}
