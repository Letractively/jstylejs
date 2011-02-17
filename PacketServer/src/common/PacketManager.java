package common;

import java.util.concurrent.LinkedBlockingQueue;

public class PacketManager {

	private LinkedBlockingQueue<Packet> receives;

	public PacketManager() {
		receives = new LinkedBlockingQueue<Packet>();
	}

	public Packet takeReceived() throws InterruptedException {
		return receives.take();
	}

	public void addReceived(Packet received) {
		try {
			this.receives.put(received);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
