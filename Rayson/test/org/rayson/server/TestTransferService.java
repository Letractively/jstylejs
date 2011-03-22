package org.rayson.server;

import java.io.IOException;

import org.rayson.annotation.TransferCode;
import org.rayson.api.TransferService;
import org.rayson.api.TransferSocket;

public class TestTransferService implements TransferService {

	@Override
	@TransferCode(10)
	public void process(TransferSocket socket) throws IOException {
		System.out.println("TransferSocket read int: "
				+ socket.getDataInput().readInt());
	}

}
