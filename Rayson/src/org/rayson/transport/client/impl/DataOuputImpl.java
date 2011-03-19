package org.rayson.transport.client.impl;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.rayson.transport.client.StreamConnection;

class DataOuputImpl implements DataOutput {

	private DataOutputStream innerOut;
	private StreamConnection connection;

	public DataOuputImpl(DataOutputStream dataOutputStream,
			StreamConnection connection) {
		this.innerOut = dataOutputStream;
		this.connection = connection;
	}

	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(byte[] b) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeByte(int v) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeBytes(String s) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeChar(int v) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeChars(String s) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeDouble(double v) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeFloat(float v) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeInt(int v) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeLong(long v) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeShort(int v) throws IOException {
		try {
			innerOut.writeShort(v);
			connection.touch();
		} catch (IOException e) {
			catchIOExcepion(e);
		}
	}

	private void catchIOExcepion(IOException e) throws IOException {
		connection.close();
		connection.remove();
		throw e;
	}

	@Override
	public void writeUTF(String s) throws IOException {
		// TODO Auto-generated method stub

	}

}
