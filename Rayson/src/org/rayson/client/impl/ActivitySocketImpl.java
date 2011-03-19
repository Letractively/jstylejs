package org.rayson.client.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import org.rayson.api.ActivitySocket;

public class ActivitySocketImpl implements ActivitySocket {

	private SocketChannel socketChannel;
	private DataInputStream in;
	private DataOutputStream out;
	private byte version;
	private short activity;

	public ActivitySocketImpl(SocketChannel socketChannel, byte version,
			short activity) throws IOException {
		this.socketChannel = socketChannel;
		this.in = new DataInputStream(this.socketChannel.socket()
				.getInputStream());
		this.out = new DataOutputStream(this.socketChannel.socket()
				.getOutputStream());
		this.version = version;
		this.activity = activity;
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
		return activity;
	}

	@Override
	public byte getVersion() {
		return version;
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