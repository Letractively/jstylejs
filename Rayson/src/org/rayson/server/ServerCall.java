package org.rayson.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.common.InvocationResultType;
import org.rayson.common.Stream;
import org.rayson.exception.CallParameterException;
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
	private InvocationException exception;

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

	public static ServerCall fromPacket(Packet requestPacket) {

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
		Invocation invocation = new Invocation();
		try {
			invocation.read(dataInputStream);
			serverCall.invocation = invocation;
		} catch (IOException e) {
			serverCall.setException(new InvocationException(true,
					new CallParameterException("Read call invocation error: "
							+ e.toString())));
		}
		return serverCall;
	}

	public Packet getResponsePacket() throws PacketException {
		if (responsePacket == null) {
			responsePacket = toResponsePacket();
		}
		return responsePacket;
	}

	public boolean exceptionSetted() {
		return (exception != null);
	}

	private Packet toResponsePacket() throws PacketException {
		Packet packet = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
				BUFFER_SIZE);
		DataOutputStream dataOutputStream = new DataOutputStream(
				byteArrayOutputStream);
		try {

			dataOutputStream.writeLong(clientCallId);

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packet;
	}

	void setResult(Object result) {
		this.result = result;
	}

	void setException(InvocationException exception) {
		this.exception = exception;
	}
}
