package org.rayson.api;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.SocketAddress;

public interface ActivitySocket {

	public DataInput getDataInput();

	public DataOutput getDataOutput();

	public void shutdownInput() throws IOException;

	public void shutdownOutput() throws IOException;

	public boolean isClosed();

	public boolean isInputShutdown();

	public boolean isOutputShutdown();

	public void close() throws IOException;

	public short getActivity();

	public short getVersion();

	public SocketAddress getLocalAddr();

	public SocketAddress getRemoteAddr();

}