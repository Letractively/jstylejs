package common;

import java.util.concurrent.LinkedBlockingQueue;

public class PacketSender extends Thread {
	private LinkedBlockingQueue<RpcPacket> sendingPackets;

	PacketSender(CallManager callManager) {
		this.sendingPackets = new LinkedBlockingQueue<RpcPacket>();
	}

	@Override
	public void run() {
		RpcPacket packet;
		while (true) {
			try {
				packet = sendingPackets.take();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
	}
}
