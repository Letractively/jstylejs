package common;

import java.io.IOException;

public interface Connection {
	public void close() throws IOException;

	public boolean isTimeOut();

	/**
	 * Try to read {@link Packet} out from this connection.
	 * 
	 * @throws IOException
	 */
	public int read() throws IOException;

	public void touch();

	public byte getProtocol();

	public int getVersion();

	/**
	 * Try to write last {@link Packet} to socket of this connection.
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException;

	public long getId();

	public void addSendPacket(Packet responsePacket) throws IOException;

}
