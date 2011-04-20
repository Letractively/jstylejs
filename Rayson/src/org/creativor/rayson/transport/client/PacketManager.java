/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.client;

import java.util.concurrent.LinkedBlockingQueue;
import org.creativor.rayson.transport.common.Packet;

/**
 *
 * @author Nick Zhang
 */
public class PacketManager {

	private LinkedBlockingQueue<Packet> receives;

	public PacketManager() {
		receives = new LinkedBlockingQueue<Packet>();
	}

	public void addReceived(Packet received) {
		try {
			this.receives.put(received);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Packet takeReceived() throws InterruptedException {
		return receives.take();
	}

}
