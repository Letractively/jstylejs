package org.creativor.rayson.api;

import java.io.IOException;

public interface TransferService<T extends TransferArgument> {
	public void process(T argument, TransferSocket socket) throws IOException;

	/**
	 * Check whether the client version is supported by this service.
	 * 
	 * @param clientVersion
	 * @return True is the client version is supported by this service.
	 */
	public boolean isSupportedVersion(short clientVersion);

}
