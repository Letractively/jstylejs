package common;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractConnection implements Connection {
	private SocketChannel socketChannel;
	private long id;
	private static AtomicLong UID = new AtomicLong(0);

	protected AbstractConnection() {
		this.id = UID.getAndIncrement();
	}

	public long getId() {
		return id;
	}

	@Override
	public void close() throws IOException {

	}

	protected SocketChannel getSocketChannel() {
		return socketChannel;
	}

	@Override
	public boolean isTimeOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read() throws IOException {
	}

	@Override
	public void touch() {
		// TODO Auto-generated method stub

	}

	protected void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public void write() throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id: ");
		sb.append(this.id);
		sb.append(", address: ");
		sb.append(this.socketChannel.socket().toString());
		sb.append("}");
		return sb.toString();
	}

}
