package org.rayson.demo.explorer.server;

import java.io.IOException;

import org.rayson.annotation.TransferCode;
import org.rayson.api.TransferService;
import org.rayson.api.TransferSocket;

class DownloadTransfer implements TransferService {

	@Override
	@TransferCode(1)
	public void process(TransferSocket socket) throws IOException {

	}

}
