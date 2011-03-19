package org.rayson.transport.stream;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.SocketAddress;

import org.rayson.api.ActivitySocket;
import org.rayson.transport.client.ClientStreamConnection;

public class ActivitySocketImpl implements ActivitySocket {

	private DataInput in;
	private DataOutput out;
	private ClientStreamConnection connection;

	public ActivitySocketImpl(ClientStreamConnection connection)
			throws IOException {
		this.in = new DataInputImpl(connection.getInputBuffer());
		this.out = new DataOuputImpl(connection.getOutputBuffer());
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
		// TODO:
		return null;
	}

	@Override
	public SocketAddress getRemoteAddr() {
		// TODO:
		return null;
	}
}