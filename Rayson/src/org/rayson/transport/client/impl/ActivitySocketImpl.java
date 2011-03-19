package org.rayson.transport.client.impl;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import org.rayson.api.ActivitySocket;
import org.rayson.transport.client.StreamConnection;

public class ActivitySocketImpl implements ActivitySocket {

	private SocketChannel socketChannel;
	private DataInput in;
	private DataOutput out;
	private StreamConnection connection;
	private short activity;

	public ActivitySocketImpl(StreamConnection connection,
			SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		this.in = new DataInputImpl(new DataInputStream(this.socketChannel
				.socket().getInputStream()), connection);
		this.out = new DataOuputImpl(new DataOutputStream(this.socketChannel
				.socket().getOutputStream()), connection);
		this.connection = connection;
		this.out.writeShort(connection.getActivity());
	}

	@Override
	public DataInput getDataInput() {
		return in;
	}

	@Override
	public DataOutput getDataOutput() {
		return out;
	}

	@Override
	public void close() throws IOException {
		this.connection.close();
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