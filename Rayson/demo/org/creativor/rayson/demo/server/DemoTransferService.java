/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.demo.server;

import java.io.IOException;
import org.creativor.rayson.api.TransferService;
import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.demo.api.DemoTransferArgument;

/**
 *
 * @author Nick Zhang
 */
public class DemoTransferService implements
		TransferService<DemoTransferArgument> {

	@Override
	public void process(DemoTransferArgument argument, TransferSocket socket)
			throws IOException {
		System.out.println("TransferSocket read int: "
				+ socket.getDataInput().readInt());
	}

	@Override
	public boolean isSupportedVersion(short clientVersion) {
		return false;
	}
}