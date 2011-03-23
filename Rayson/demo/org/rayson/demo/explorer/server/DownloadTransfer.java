package org.rayson.demo.explorer.server;

import java.io.IOException;

import org.rayson.annotation.TransferCode;
import org.rayson.api.TransferService;
import org.rayson.api.TransferSocket;
import org.rayson.demo.explorer.api.DownloadArgument;

@TransferCode(1)
class DownloadTransfer implements TransferService<DownloadArgument> {

	@Override
	public void process(DownloadArgument argument, TransferSocket socket)
			throws IOException {

	}

}
