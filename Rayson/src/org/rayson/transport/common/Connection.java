package org.rayson.transport.common;

import java.io.IOException;

public interface Connection {
	public void addSendPacket(Packet responsePacket) throws IOException;

	public void close() throws IOException;

	public long getId();

	public ProtocolType getProtocol();

	public int getVersion();

	void init() throws IOException;

	public boolean isTimeOut();

	int pendingPacketCount();

	/**
	 * Try to read {@link Packet} out from this connection.
	 * 
	 * @throws IOException
	 */
	public int read() throws IOException;

	public void touch();

	/**
	 * Try to write last {@link Packet} to socket of this connection.
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException;

}
