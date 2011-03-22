package org.rayson.transport.stream;

import java.io.DataOutput;
import java.io.IOException;

import org.rayson.api.TransferSocket;

class DataOutputImpl implements DataOutput {

	private DataOutput innerOutput;
	private TransferSocket transferSocket;

	public DataOutputImpl(DataOutput dataOutput, TransferSocket transferSocket) {
		this.innerOutput = dataOutput;
		this.transferSocket = transferSocket;
	}

	private void catchIOException(IOException e) throws IOException {
		try {
			this.transferSocket.close();
		} finally {

		}
		throw e;
	}

	@Override
	public void write(byte[] b) throws IOException {
		try {
			innerOutput.write(b);
		} catch (IOException e) {
			catchIOException(e);
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		try {
			innerOutput.write(b);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void write(int b) throws IOException {
		try {
			innerOutput.write(b);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		try {
			innerOutput.writeBoolean(v);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeByte(int v) throws IOException {
		try {
			innerOutput.writeByte(v);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeBytes(String s) throws IOException {
		try {
			innerOutput.writeBytes(s);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeChar(int v) throws IOException {
		try {
			innerOutput.writeChar(v);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeChars(String s) throws IOException {
		try {
			innerOutput.writeChars(s);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeDouble(double v) throws IOException {
		try {
			innerOutput.writeDouble(v);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeFloat(float v) throws IOException {
		try {
			innerOutput.writeFloat(v);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeInt(int v) throws IOException {
		try {
			innerOutput.writeInt(v);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeLong(long v) throws IOException {
		try {
			innerOutput.writeLong(v);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeShort(int v) throws IOException {
		try {
			innerOutput.writeShort(v);
		} catch (IOException e) {
			catchIOException(e);
		}

	}

	@Override
	public void writeUTF(String s) throws IOException {
		try {
			innerOutput.writeUTF(s);
		} catch (IOException e) {
			catchIOException(e);
		}

	}
}