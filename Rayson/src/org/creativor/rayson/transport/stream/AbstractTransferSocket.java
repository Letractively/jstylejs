/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.stream;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.transport.api.TimeLimitConnection;

public abstract class AbstractTransferSocket implements TransferSocket {

	private DataInput dataInput;
	private DataOutput dataOutput;
	private Socket socket;
	private short code;
	private short clientVersion;
	private byte connectionVersion;

	protected AbstractTransferSocket(TimeLimitConnection connection,
			Socket socket, short code, short clientVersion) throws IOException {
		this.socket = socket;
		this.clientVersion = clientVersion;
		this.connectionVersion = connection.getVersion();
		this.code = code;
		this.dataInput = new DataInputImpl(new DataInputStream(
				this.socket.getInputStream()), this, connection);
		this.dataOutput = new DataOutputImpl(new DataOutputStream(
				this.socket.getOutputStream()), this, connection);
	}

	@Override
	public final byte getConnectionVersion() {
		return connectionVersion;
	}

	@Override
	public void close() throws IOException {
		this.socket.close();
	}

	@Override
	public DataInput getDataInput() {
		return dataInput;
	}

	@Override
	public DataOutput getDataOutput() {
		return dataOutput;
	}

	@Override
	public SocketAddress getLocalAddr() {
		return socket.getLocalSocketAddress();
	}

	@Override
	public SocketAddress getRemoteAddr() {
		return socket.getRemoteSocketAddress();
	}

	@Override
	public final short getCode() {
		return code;
	}

	@Override
	public final short getClientVersion() {
		return clientVersion;
	}

	@Override
	public boolean isClosed() {
		return socket.isClosed();
	}

	@Override
	public boolean isInputShutdown() {
		return socket.isInputShutdown();
	}

	@Override
	public boolean isOutputShutdown() {
		return socket.isOutputShutdown();
	}

	@Override
	public void shutdownInput() throws IOException {
		this.socket.shutdownInput();
	}

	@Override
	public void shutdownOutput() throws IOException {
		this.socket.shutdownOutput();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("code: ");
		sb.append(this.code);
		sb.append(", ");
		sb.append("connection version: ");
		sb.append(this.connectionVersion);
		sb.append(", ");
		sb.append("client version: ");
		sb.append(this.clientVersion);
		sb.append(", ");
		sb.append("socket: ");
		sb.append(this.socket.toString());
		sb.append("}");
		return sb.toString();
	}
}