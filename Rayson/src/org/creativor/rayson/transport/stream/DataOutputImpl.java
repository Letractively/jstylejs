/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.stream;

import java.io.DataOutput;
import java.io.IOException;

import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.transport.api.TimeLimitConnection;

class DataOutputImpl extends DataStreamer implements DataOutput {

	private DataOutput innerOutput;

	public DataOutputImpl(DataOutput dataOutput, TransferSocket transferSocket,
			TimeLimitConnection connection) {
		super(transferSocket, connection);
		this.innerOutput = dataOutput;
	}

	@Override
	public void write(byte[] b) throws IOException {
		try {
			innerOutput.write(b);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		try {
			innerOutput.write(b);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void write(int b) throws IOException {
		try {
			innerOutput.write(b);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		try {
			innerOutput.writeBoolean(v);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeByte(int v) throws IOException {
		try {
			innerOutput.writeByte(v);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeBytes(String s) throws IOException {
		try {
			innerOutput.writeBytes(s);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeChar(int v) throws IOException {
		try {
			innerOutput.writeChar(v);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeChars(String s) throws IOException {
		try {
			innerOutput.writeChars(s);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeDouble(double v) throws IOException {
		try {
			innerOutput.writeDouble(v);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeFloat(float v) throws IOException {
		try {
			innerOutput.writeFloat(v);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeInt(int v) throws IOException {
		try {
			innerOutput.writeInt(v);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeLong(long v) throws IOException {
		try {
			innerOutput.writeLong(v);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeShort(int v) throws IOException {
		try {
			innerOutput.writeShort(v);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void writeUTF(String s) throws IOException {
		try {
			innerOutput.writeUTF(s);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}
}