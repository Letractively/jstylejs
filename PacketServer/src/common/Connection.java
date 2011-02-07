package common;

import java.io.IOException;

public interface Connection {
	public void close() throws IOException;

	public boolean isTimeOut();

	/**
	 * Try to read {@link RpcPacket} out from this connection.
	 * 
	 * @throws IOException
	 */
	public void read() throws IOException;

	public void touch();

	/**
	 * Try to write last {@link RpcPacket} to socket of this connection.
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException;

	public long getId();

}
