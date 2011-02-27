package org.rayson.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.common.InvocationResult;
import org.rayson.common.Stream;
import org.rayson.transport.common.Packet;
import org.rayson.transport.common.PacketException;

public class ServerCall {

	private static final int BUFFER_SIZE = 1024;
	private long id;
	private long clientCallId;
	private static final AtomicLong UID = new AtomicLong(0);
	private Invocation invocation;
	private Object result;
	private Packet responsePacket;
	private InvocationException remoteExceptionHandler;

	private ServerCall() {
		this.id = UID.getAndIncrement();
	}

	public long getId() {
		return id;
	}

	public long getClientId() {
		return clientCallId;
	}

	Invocation getInvocation() {
		return invocation;
	}

	public static ServerCall fromPacket(Packet requestPacket)
			throws IOException {

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				requestPacket.getData());
		DataInputStream dataInputStream = new DataInputStream(
				byteArrayInputStream);
		long clientCallId = dataInputStream.readLong();
		Invocation invocation = new Invocation();
		invocation.read(dataInputStream);
		ServerCall serverCall = new ServerCall();
		serverCall.clientCallId = clientCallId;
		serverCall.invocation = invocation;
		return serverCall;
	}

	public Packet getResponsePacket() throws PacketException {
		if (responsePacket == null) {
			responsePacket = toResponsePacket();
		}
		return responsePacket;
	}

	private Packet toResponsePacket() throws PacketException {
		Packet packet = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
				BUFFER_SIZE);
		DataOutputStream dataOutputStream = new DataOutputStream(
				byteArrayOutputStream);
		try {

			dataOutputStream.writeLong(clientCallId);

			if (remoteExceptionHandler != null) {
				dataOutputStream.writeByte(InvocationResult.EXCEPTION.getType());
				remoteExceptionHandler.write(dataOutputStream);
			} else {
				dataOutputStream.writeByte(InvocationResult.SUCCESSFUL.getType());
				Stream.writePortable(dataOutputStream, result);
			}
			packet = new Packet(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packet;
	}

	void setResult(Object result) {
		this.result = result;
	}

	void setException(boolean unDeclaredException, Throwable t) {
		this.remoteExceptionHandler = new InvocationException(
				unDeclaredException, t);
	}
}
