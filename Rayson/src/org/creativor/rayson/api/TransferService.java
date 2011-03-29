package org.creativor.rayson.api;

import java.io.IOException;

public interface TransferService<T extends TransferArgument> {
	public void process(T argument, TransferSocket socket) throws IOException;
}
