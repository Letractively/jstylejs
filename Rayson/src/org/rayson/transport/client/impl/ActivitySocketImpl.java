package org.rayson.transport.client.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import org.rayson.api.ActivitySocket;
import org.rayson.transport.client.StreamConnection;

public class ActivitySocketImpl implements ActivitySocket {

	private SocketChannel socketChannel;
	private DataInputStream in;
	private DataOutputStream out;
	private StreamConnection connection;

	private ActivitySocketImpl(SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		this.in = new DataInputStream(this.socketChannel.socket()
				.getInputStream());
		this.out = new DataOutputStream(this.socketChannel.socket()
				.getOutputStream());
	}

	public ActivitySocketImpl(StreamConnection connection,
			SocketChannel socketChannel) throws IOException {
		this(socketChannel);
		this.connection = connection;
	}

	@Override
	public DataInputStream getInputStream() throws IOException {
		return in;
	}

	@Override
	public DataOutputStream getOutputStream() throws IOException {
		return out;
	}

	@Override
	public void close() throws IOException {

		this.socketChannel.close();
	}

	@Override
	public short getActivity() {
		return this.connection.getActivity();
	}

	@Override
	public short getVersion() {
		return this.connection.getVersion();
	}

	@Override
	public SocketAddress getLocalAddr() {
		return this.socketChannel.socket().getLocalSocketAddress();
	}

	@Override
	public SocketAddress getRemoteAddr() {
		return this.socketChannel.socket().getRemoteSocketAddress();
	}
}