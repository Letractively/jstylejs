package org.creativor.rayson.server;

import java.io.IOException;

import org.creativor.rayson.api.TestTransferArgument;
import org.creativor.rayson.api.TransferService;
import org.creativor.rayson.api.TransferSocket;

public class TestTransferService implements
		TransferService<TestTransferArgument> {

	@Override
	public void process(TestTransferArgument argument, TransferSocket socket)
			throws IOException {
		System.out.println("TransferSocket read int: "
				+ socket.getDataInput().readInt());
	}
}
