package org.rayson.transport.stream;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class StreamInputBuffer {

	private ByteBuffer bb;
	private SocketChannel socketChannel;

	public StreamInputBuffer(SocketChannel socketChannel, int bufferSize) {
		this.socketChannel = socketChannel;
		this.bb = ByteBuffer.allocate(bufferSize);
	}

	public void close() {

	}

	public int asyncReadChannel() {
		return 0;
	}

	public boolean readBoolean() throws BufferClosedException {
		// TODO Auto-generated method stub
		return false;
	}

	public byte readByte() throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public char readChar() throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public double readDouble() throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public float readFloat() throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void readFully(byte[] arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void readFully(byte[] arg0, int arg1, int arg2)
			throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public int readInt() throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String readLine() throws BufferClosedException {
		// TODO Auto-generated method stub
		return null;
	}

	public long readLong() throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public short readShort() throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String readUTF() throws BufferClosedException {
		// TODO Auto-generated method stub
		return null;
	}

	public int readUnsignedByte() throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int readUnsignedShort() throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int skipBytes(int arg0) throws BufferClosedException {
		// TODO Auto-generated method stub
		return 0;
	}

}
