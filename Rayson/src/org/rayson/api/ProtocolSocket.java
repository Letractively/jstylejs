package org.rayson.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;

public interface ProtocolSocket {

	public DataInputStream getInputStream() throws IOException;

	public DataOutputStream getOutputStream() throws IOException;

	public void close() throws IOException;

	public short getProtocol();

	public byte getVersion();

	public SocketAddress getLocalAddr();

	public SocketAddress getRemoteAddr();

}
