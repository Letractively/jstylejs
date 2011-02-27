package org.rayson.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.common.InvocationResult;
import org.rayson.common.Stream;
import org.rayson.transport.common.Packet;
import org.rayson.transport.common.PacketException;

public class ClientCall<V> {
	private static final AtomicLong UID = new AtomicLong(0);
	private CallFuture<V> future;
	private long id;
	private Invocation invocation;
	private static final int BUFFER_SIZE = 1024;
	private Packet requestPacket;

	public ClientCall(Invocation invocation) throws PacketException {
		this.id = UID.getAndIncrement();
		this.invocation = invocation;
		this.future = new CallFuture<V>();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
				BUFFER_SIZE);
		DataOutputStream dataOutputStream = new DataOutputStream(
				byteArrayOutputStream);
		try {
			dataOutputStream.writeLong(id);
			invocation.write(dataOutputStream);
			requestPacket = new Packet(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public long getId() {
		return id;
	}

	public Packet getRequestPacket() {
		return this.requestPacket;
	}

	public V getResult() throws InterruptedException, ExecutionException {
		return future.get();
	}

	public void readResult(DataInput in) throws IOException {
		InvocationResult responseState = InvocationResult.valueOf(in.readByte());
		switch (responseState) {
		case SUCCESSFUL:
			this.future.set((V) Stream.readPortable(in));
			break;
		case EXCEPTION:
			InvocationException remoteExceptionHandler = new InvocationException();
			remoteExceptionHandler.read(in);
			this.future
					.setException(remoteExceptionHandler.getException());
			break;
		default:
			break;
		}
	}
}
