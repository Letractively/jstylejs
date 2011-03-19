package org.rayson.transport.api;

import java.io.IOException;

import org.rayson.transport.common.ProtocolType;

public interface Connection {
	public void close() throws IOException;

	public long getId();

	public ProtocolType getProtocol();

	public short getVersion();

	public int read() throws IOException;

	public void write() throws IOException;

}
