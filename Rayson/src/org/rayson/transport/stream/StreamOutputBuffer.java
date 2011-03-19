package org.rayson.transport.stream;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class StreamOutputBuffer {

	private ByteBuffer bb;
	private SocketChannel socketChannel;
	private SelectionKey selectionkey;

	public StreamOutputBuffer(SocketChannel socketChannel,
			SelectionKey selectionKey, int bufferSize) {
		this.socketChannel = socketChannel;
		this.selectionkey = selectionKey;
		this.bb = ByteBuffer.allocate(bufferSize);
	}

	public int asyncWriteChannel() {
		return 0;
	}

	public void close() {

	}

	public void write(int arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void write(byte[] arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void write(byte[] arg0, int arg1, int arg2)
			throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeBoolean(boolean arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeByte(int arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeBytes(String arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeChar(int arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeChars(String arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeDouble(double arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeFloat(float arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeInt(int arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeLong(long arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeShort(int arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

	public void writeUTF(String arg0) throws BufferClosedException {
		// TODO Auto-generated method stub

	}

}
