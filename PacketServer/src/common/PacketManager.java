package common;

import java.util.concurrent.LinkedBlockingQueue;

public class PacketManager {
	private LinkedBlockingQueue<RpcPacket> receives;

	public PacketManager() {
		receives = new LinkedBlockingQueue<RpcPacket>();
	}

	public RpcPacket takeReceived() throws InterruptedException {
		return receives.take();
	}

	public void addReceived(RpcPacket received) {
		try {
			this.receives.put(received);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
