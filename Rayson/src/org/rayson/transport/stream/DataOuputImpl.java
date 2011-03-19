package org.rayson.transport.stream;

import java.io.DataOutput;
import java.io.IOException;

class DataOuputImpl implements DataOutput {

	private StreamOutputBuffer outputBuffer;

	public DataOuputImpl(StreamOutputBuffer outBuffer) {
		this.outputBuffer = outBuffer;
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
			this.outputBuffer.writeShort(v);
		} catch (BufferClosedException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void writeUTF(String s) throws IOException {
		// TODO Auto-generated method stub

	}

}
