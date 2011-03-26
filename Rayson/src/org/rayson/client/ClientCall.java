package org.rayson.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.api.CallFuture;
import org.rayson.client.impl.CallFutureImpl;
import org.rayson.common.ClientSession;
import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.common.InvocationResultType;
import org.rayson.common.Stream;
import org.rayson.exception.CallException;
import org.rayson.exception.RpcException;
import org.rayson.transport.common.Packet;
import org.rayson.transport.common.PacketException;

public class ClientCall<V> {
	private static final int BUFFER_SIZE = 1024;
	private static final AtomicLong UID = new AtomicLong(0);
	private CallFutureImpl<V> future;
	private long id;
	private Invocation invocation;
	private Packet requestPacket;
	private ClientSession session;

	public ClientCall(ClientSession session, Invocation invocation)
			throws PacketException {
		this.id = UID.getAndIncrement();
		this.session = session;
		this.invocation = invocation;
		this.future = new CallFutureImpl<V>();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
				BUFFER_SIZE);
		DataOutputStream dataOutputStream = new DataOutputStream(
				byteArrayOutputStream);
		try {
			dataOutputStream.writeLong(id);
			session.write(dataOutputStream);
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

	public Invocation getInvocation() {
		return invocation;
	}

	public Packet getRequestPacket() {
		return this.requestPacket;
	}

	public CallFuture<V> getFuture() {
		return future;
	}

	public V getResult() throws InterruptedException, RpcException {
		return future.get();
	}

	public void notifyConnectionClosed() {
		this.future.setException(new InvocationException(false,
				new ConnectionClosedException()));
	}

	public void readResult(DataInput in) {
		InvocationResultType resultType;
		try {
			resultType = InvocationResultType.valueOf(in.readByte());

			switch (resultType) {
			case SUCCESSFUL:
				this.future.set((V) Stream.readPortable(in));
				break;
			case EXCEPTION:
				InvocationException remoteExceptionHandler = new InvocationException();
				remoteExceptionHandler.read(in);
				this.future.setException(remoteExceptionHandler);
				break;
			default:
				break;
			}
		} catch (IOException e) {
			this.future.setException(new InvocationException(false,
					new CallException("Read call result error:"
							+ e.getMessage())));
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id: ");
		sb.append(id);
		sb.append(", session: ");
		sb.append(this.session.toString());
		sb.append(", invocation: ");
		sb.append(this.invocation.toString());
		sb.append("}");
		return sb.toString();
	}
}
