package org.rayson.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;

public interface ActivitySocket {

	public DataInputStream getInputStream() throws IOException;

	public DataOutputStream getOutputStream() throws IOException;

	public void close() throws IOException;

	public short getActivity();

	public short getVersion();

	public SocketAddress getLocalAddr();

	public SocketAddress getRemoteAddr();

}
