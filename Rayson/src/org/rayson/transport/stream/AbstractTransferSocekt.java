package org.rayson.transport.stream;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

import org.rayson.api.TransferSocket;

public abstract class AbstractTransferSocekt implements TransferSocket {

	private Socket socket;
	private short version;
	private short transfer;
	private DataInput dataInput;
	private DataOutput dataOutput;

	protected AbstractTransferSocekt(Socket socket, short transfer,
			short version) throws IOException {
		this.socket = socket;
		this.version = version;
		this.transfer = transfer;
		this.dataInput = new DataInputStream(this.socket.getInputStream());
		this.dataOutput = new DataOutputStream(this.socket.getOutputStream());
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
	public void shutdownInput() throws IOException {
		this.socket.shutdownInput();
	}

	@Override
	public void shutdownOutput() throws IOException {
		this.socket.shutdownOutput();
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
	public void close() throws IOException {
		this.socket.close();
	}

	@Override
	public short getTransfer() {
		return transfer;
	}

	@Override
	public short getVersion() {
		return version;
	}

	@Override
	public SocketAddress getLocalAddr() {
		return socket.getLocalSocketAddress();
	}

	@Override
	public SocketAddress getRemoteAddr() {
		return socket.getRemoteSocketAddress();
	}
}