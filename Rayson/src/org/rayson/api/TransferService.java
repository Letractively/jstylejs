package org.rayson.api;

import java.io.IOException;

public interface TransferService {
	public void process(TransferSocket socket) throws IOException;
}
