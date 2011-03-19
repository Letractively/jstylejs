package org.rayson.transport.api;

import org.rayson.transport.stream.StreamInputBuffer;
import org.rayson.transport.stream.StreamOutputBuffer;

public interface StreamConnection extends Connection {
	public StreamInputBuffer getInputBuffer();

	public StreamOutputBuffer getOutputBuffer();
}
