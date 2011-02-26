package org.rayson.server;

import java.util.concurrent.atomic.AtomicLong;

import org.rayson.io.Invocation;
import org.rayson.transport.common.Packet;

public class ServerCall {
	private static final AtomicLong UID = new AtomicLong(0);
	private long id;
	private Invocation invocation;

	public ServerCall(Packet requsetPacket) {
		this.id = UID.getAndIncrement();
	}

	public long getId() {
		return id;
	}

}
