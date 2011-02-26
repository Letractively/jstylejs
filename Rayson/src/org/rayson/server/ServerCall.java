package org.rayson.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.io.PortableObject;
import org.rayson.io.Invocation;
import org.rayson.io.RemoteExceptionHandler;
import org.rayson.io.ResponseState;
import org.rayson.io.UnsupportedIOObjectException;
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
	private RemoteExceptionHandler remoteExceptionHandler;

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

	public Packet getResponsePacket() throws UnsupportedIOObjectException,
			PacketException {
		if (responsePacket == null) {
			responsePacket = toResponsePacket();
		}
		return responsePacket;
	}

	private Packet toResponsePacket() throws UnsupportedIOObjectException,
			PacketException {
		Packet packet = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
				BUFFER_SIZE);
		DataOutputStream dataOutputStream = new DataOutputStream(
				byteArrayOutputStream);
		try {

			dataOutputStream.writeLong(clientCallId);

			if (remoteExceptionHandler != null) {
				dataOutputStream.writeByte(ResponseState.EXCEPTION.getState());
				remoteExceptionHandler.write(dataOutputStream);
			} else {
				dataOutputStream.writeByte(ResponseState.SUCCESSFUL.getState());
				PortableObject ioObject = PortableObject.objectOf(result);
				// write io object type.
				dataOutputStream.writeShort(ioObject.getType());
				// write result it self.
				ioObject.write(dataOutputStream, result);
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
		this.remoteExceptionHandler = new RemoteExceptionHandler(
				unDeclaredException, t);
	}
}
