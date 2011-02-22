package common;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class PacketReader {

	private enum State {
		CODE, DATA_LENGTH, CHECKSUM, DATA;
	}

	private State state;
	private SocketChannel socketChannel;

	public PacketReader(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		state = State.CODE;
	}

	public int read() throws IOException {
		return 0;
	}

	private Packet get() throws ChecksumErrorException {
		return null;
	}
}
