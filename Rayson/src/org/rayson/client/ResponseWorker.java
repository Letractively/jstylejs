package org.rayson.client;

import java.util.logging.Logger;

import org.rayson.transport.client.TransportClient;
import org.rayson.util.Log;

class ResponseWorker extends Thread {

	private static Logger LOGGER = Log.getLogger();

	ResponseWorker() {
		setName("Client rpc call response worker");
	}

	@Override
	public void run() {
		LOGGER.info(getName() + " starting...");
		while (true) {
			try {
				TransportClient.getSingleton().getConnector().responseOneCall();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		LOGGER.info(getName() + " stopped");

	}
}
