package org.rayson.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.common.ClientSession;
import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.common.InvocationResultType;
import org.rayson.common.Stream;
import org.rayson.exception.CallException;
import org.rayson.server.api.RpcSession;
import org.rayson.server.impl.RpcSessionImpl;
import org.rayson.transport.common.Packet;

public class ServerCall {

	private static final int BUFFER_SIZE = 1024;
	private static final AtomicLong UID = new AtomicLong(0);
	private RpcSession session;

	public static ServerCall fromPacket(SocketAddress remoteAddress,
			Packet requestPacket) {

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				requestPacket.getData());
		DataInputStream dataInputStream = new DataInputStream(
				byteArrayInputStream);
		ServerCall serverCall = new ServerCall();
		long clientCallId;
		try {
			clientCallId = dataInputStream.readLong();
		} catch (IOException e) {
			// can not get client call id, so we need to ignore this packet.
			return null;
		}
		serverCall.clientCallId = clientCallId;
		try {
			ClientSession clientSession = new ClientSession();
			clientSession.read(dataInputStream);
			RpcSession rpcSession = new RpcSessionImpl(clientSession,
					remoteAddress);
			serverCall.session = rpcSession;
			Invocation invocation = new Invocation();
			invocation.read(dataInputStream);
			serverCall.invocation = invocation;
		} catch (Throwable e) {
			serverCall.setException(new InvocationException(false,
					new CallException("Read call invocation error: "
							+ e.toString())));
		}
		return serverCall;
	}

	private long clientCallId;
	private InvocationException exception;
	private long id;
	private Invocation invocation;
	private Packet responsePacket;
	private Object result;

	private ServerCall() {
		this.id = UID.getAndIncrement();
	}

	public boolean exceptionCatched() {
		return (exception != null);
	}

	public long getClientId() {
		return clientCallId;
	}

	public long getId() {
		return id;
	}

	Invocation getInvocation() {
		return invocation;
	}

	/**
	 * @return
	 * @throws RuntimeException
	 *             If caught a runtime exception.
	 */
	public Packet getResponsePacket() {
		if (responsePacket == null) {
			responsePacket = toResponsePacket();
		}
		return responsePacket;
	}

	public RpcSession getSession() {
		return session;
	}

	void setException(InvocationException exception) {
		this.exception = exception;
	}

	void setResult(Object result) {
		this.result = result;
	}

	private Packet toResponsePacket() {
		return toResponsePacket(1);
	}

	private Packet toResponsePacket(int tryTime) {

		Packet packet = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
				BUFFER_SIZE);
		DataOutputStream dataOutputStream = new DataOutputStream(
				byteArrayOutputStream);
		try {
			dataOutputStream.writeLong(clientCallId);
		} catch (IOException e) {
			throw new RuntimeException(
					"Can not write client call id to data output stream", e);
		}
		try {

			if (exception != null) {
				dataOutputStream.writeByte(InvocationResultType.EXCEPTION
						.getType());
				exception.write(dataOutputStream);
			} else {
				dataOutputStream.writeByte(InvocationResultType.SUCCESSFUL
						.getType());
				Stream.writePortable(dataOutputStream, result);
			}
			packet = new Packet(byteArrayOutputStream.toByteArray());
		} catch (Exception e) {
			if (tryTime > 3)
				throw new RuntimeException("Try to write call result "
						+ tryTime + " times, but still failed!");
			// return a call exception packet
			exception = new InvocationException(true, new CallException(
					"Write call result error:" + e.getMessage()));
			return toResponsePacket(++tryTime);
		}
		return packet;
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
