package org.rayson.client;

import java.io.IOException;
import java.util.logging.Logger;

import org.rayson.transport.client.TransportClient;

class ResponseWorker extends Thread {

	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	ResponseWorker() {
		setName("Client rpc call response worker");
	}

	@Override
	public void run() {
		LOGGER.info(getName() + " starting...");
		while (true) {
			try {
				try {
					TransportClient.getSingleton().getConnector().responseCall();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		LOGGER.info(getName() + " stopped");

	}
}
