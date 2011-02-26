package org.rayson.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.io.Invocation;
import org.rayson.transport.common.Packet;

public class ClientCall<V> {

	private Invocation invocation;
	private static final AtomicLong UID = new AtomicLong(0);
	private CallFuture<V> future;
	private long id;

	public ClientCall(Invocation invocation) {
		this.invocation = invocation;
		this.future = new CallFuture<V>();
		this.id = UID.getAndIncrement();
	}

	public Packet getRequestPacket() {
		// TODO Auto-generated method stub
		return null;
	}

	public V getResult() throws InterruptedException, ExecutionException {
		return future.get();
	}

	public long getId() {
		return id;
	}
}
