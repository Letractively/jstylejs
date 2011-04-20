/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.client;

import java.util.logging.Logger;
import org.creativor.rayson.transport.client.TransportClient;
import org.creativor.rayson.util.Log;

/**
 *
 * @author Nick Zhang
 */
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
