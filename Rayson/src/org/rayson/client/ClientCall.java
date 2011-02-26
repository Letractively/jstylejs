package org.rayson.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.io.Invocation;
import org.rayson.transport.common.Packet;

public class ClientCall<V> {
	private static final AtomicLong UID = new AtomicLong(0);
	private CallFuture<V> future;
	private long id;
	private Invocation invocation;

	public ClientCall(Invocation invocation) {
		this.invocation = invocation;
		this.future = new CallFuture<V>();
		this.id = UID.getAndIncrement();
	}

	public long getId() {
		return id;
	}

	public Packet getRequestPacket() {
		// TODO Auto-generated method stub
		return null;
	}

	public V getResult() throws InterruptedException, ExecutionException {
		return future.get();
	}
}
