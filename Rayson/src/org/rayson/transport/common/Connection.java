package org.rayson.transport.common;

import java.io.IOException;

public interface Connection {

	public void close() throws IOException;

	public long getId();

	public ProtocolType getProtocol();

	public int getVersion();

	/**
	 * Try to read {@link Packet} out from this connection.
	 * 
	 * @throws IOException
	 */
	public int read() throws IOException;

	public void touch();

	public abstract boolean isTimeOut();

	public long getLastContact();

	/**
	 * Try to write last {@link Packet} to socket of this connection.
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException;

}
