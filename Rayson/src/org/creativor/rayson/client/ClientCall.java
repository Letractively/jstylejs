/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.client.impl.CallFutureImpl;
import org.creativor.rayson.common.Invocation;
import org.creativor.rayson.common.InvocationException;
import org.creativor.rayson.common.InvocationResultType;
import org.creativor.rayson.common.Stream;
import org.creativor.rayson.exception.CallException;
import org.creativor.rayson.exception.CallExecutionException;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.exception.UnsupportedVersionException;
import org.creativor.rayson.transport.common.Packet;
import org.creativor.rayson.transport.common.PacketException;

/**
 *
 * @author Nick Zhang
 */
public class ClientCall<V> {
	private static final int BUFFER_SIZE = 1024;
	private static final AtomicLong UID = new AtomicLong(0);
	private CallFutureImpl<V> future;
	private long id;
	private Invocation invocation;
	private Packet requestPacket;
	private ProxySession session;

	public ClientCall(ProxySession session, Invocation invocation,
			CallFutureImpl<V> future) throws PacketException {
		this.id = UID.getAndIncrement();
		this.session = session;
		this.invocation = invocation;
		this.future = future;
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

	public V getResult() throws InterruptedException, RpcException,
			CallExecutionException {
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
				if (remoteExceptionHandler.getRemoteException() instanceof UnsupportedVersionException)
					session.getUnsupportedVersionException(remoteExceptionHandler
							.getRemoteException().getMessage());
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
