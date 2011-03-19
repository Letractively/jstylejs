package org.rayson.transport.common;

import java.io.IOException;

public interface Connection {
	public abstract boolean isTimeOut();

	public void close() throws IOException;

	public long getId();

	public ProtocolType getProtocol();

	/**
	 * Try to read {@link Packet} out from this connection.
	 * 
	 * @throws IOException
	 */
	public int read() throws IOException;

	/**
	 * Try to write last {@link Packet} to socket of this connection.
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException;

}
