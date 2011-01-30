package common;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractConnection implements Connection {
	private PacketWriter packetWriter;
	private PacketReader packetReader;
	private SocketChannel socketChannel;

	protected AbstractConnection() {
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	protected abstract void readNewCallId(long newCallId);

	protected SocketChannel getSocketChannel() {
		return socketChannel;
	}

	@Override
	public boolean isTimeOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read() throws IOException {

		PacketOperation operation = this.packetReader.getOperation();
		switch (operation) {
		case CALL_ID:
			Long newCallId = this.packetReader.readNewCall();
			if (newCallId != null) {
				// new rpc call id arrived.
				readNewCallId(newCallId);
				this.packetReader.submitLastPacket(call.getRequestPacket());
			}
			break;

		default: {
			RpcPacket packet = null;
			try {

				packet = this.packetReader.read();

			} catch (ChecksumNotMatchException e) {
				e.printStackTrace();
			}

			if (packet != null) {
				System.out.println("Get packet from client: "
						+ packet.toString());
			}
		}
			break;
		}

	}

	@Override
	public void touch() {
		// TODO Auto-generated method stub

	}

	protected void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		this.packetWriter = new PacketWriter(socketChannel);
		this.packetReader = new PacketReader(socketChannel);
	}

	@Override
	public void write() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return this.socketChannel.toString();
	}

}
