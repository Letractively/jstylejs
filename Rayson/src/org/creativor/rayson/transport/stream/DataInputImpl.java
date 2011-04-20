/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.stream;

import java.io.DataInput;
import java.io.IOException;

import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.transport.api.TimeLimitConnection;

class DataInputImpl extends DataStreamer implements DataInput {

	private DataInput innerInput;

	public DataInputImpl(DataInput dataInput, TransferSocket transferSocket,
			TimeLimitConnection connection) {
		super(transferSocket, connection);
		this.innerInput = dataInput;
	}

	@Override
	public boolean readBoolean() throws IOException {
		try {
			return innerInput.readBoolean();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return false;
	}

	@Override
	public byte readByte() throws IOException {
		try {
			return innerInput.readByte();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

	@Override
	public char readChar() throws IOException {
		try {
			return innerInput.readChar();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

	@Override
	public double readDouble() throws IOException {
		try {
			return innerInput.readDouble();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

	@Override
	public float readFloat() throws IOException {
		try {
			return innerInput.readFloat();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		try {
			innerInput.readFully(b);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		try {
			innerInput.readFully(b);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}

	}

	@Override
	public int readInt() throws IOException {
		try {
			return innerInput.readInt();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

	@Override
	public String readLine() throws IOException {
		try {
			return innerInput.readLine();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return null;
	}

	@Override
	public long readLong() throws IOException {
		try {
			return innerInput.readLong();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

	@Override
	public short readShort() throws IOException {
		try {
			return innerInput.readShort();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

	@Override
	public int readUnsignedByte() throws IOException {
		try {
			return innerInput.readUnsignedByte();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

	@Override
	public int readUnsignedShort() throws IOException {
		try {
			return innerInput.readUnsignedShort();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

	@Override
	public String readUTF() throws IOException {
		try {
			return innerInput.readUTF();
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return null;
	}

	@Override
	public int skipBytes(int n) throws IOException {
		try {
			innerInput.skipBytes(n);
		} catch (IOException e) {
			catchIOException(e);
		} finally {
			touch();
		}
		return 0;
	}

}
