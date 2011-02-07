package common;

import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketManager extends Thread {
	private LinkedBlockingQueue<RpcPacket> receives;

	private LinkedBlockingQueue<RpcPacket> tempResponses;
	private TreeMap<Long, ConnectionResponses> connectionMap;

	public PacketManager() {
		tempResponses = new LinkedBlockingQueue<RpcPacket>();
		connectionMap = new TreeMap<Long, PacketManager.ConnectionResponses>();
		setName("Packet Manager");
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

	@Override
	public void run() {
		RpcPacket packet;
		ConnectionResponses connectionResponseHolder;
		while (true) {
			try {
				packet = tempResponses.take();
				connectionResponseHolder = connectionMap.get(packet
						.getConnection().getId());
				if (connectionResponseHolder == null) {
					// add new one.
					connectionResponseHolder = new ConnectionResponses(
							packet.getConnection());
					connectionMap.put(
							connectionResponseHolder.getConnectionId(),
							connectionResponseHolder);
				}
				// add packet to the connection packet holder.
				connectionResponseHolder.addResponse(packet);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}

		}
	}

	public void addResponse(RpcPacket response) {
		try {
			this.tempResponses.put(response);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class PacketHolder {
		private RpcPacket packet;
		private boolean received;

		PacketHolder(RpcPacket packet, boolean received) {
			this.packet = packet;
			this.received = received;
		}

		public boolean isReceived() {
			return received;
		}

		public RpcPacket getPacket() {
			return packet;
		}

	}

	private static class ConnectionResponses {
		private Connection connection;
		private LinkedBlockingQueue<RpcPacket> responses;

		ConnectionResponses(Connection connection) {
			this.connection = connection;
			responses = new LinkedBlockingQueue<RpcPacket>();
		}

		public void addResponse(RpcPacket response) {
			try {
				this.responses.put(response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public long getConnectionId() {
			return this.connection.getId();
		}
	}

}
