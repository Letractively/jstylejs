package server;

import java.nio.channels.SocketChannel;

import common.CRC16;
import common.ChecksumMatchException;
import common.PacketException;
import common.PacketReader;
import common.RequestCode;

class ServerPacketReader extends PacketReader {

	public ServerPacketReader(SocketChannel socketChannel) {
		super(socketChannel);
	}

	@Override
	protected void verifyPacket(byte code, short dataLength, short checksum,
			byte[] data) throws ChecksumMatchException, PacketException {
		RequestCode requestCode = RequestCode.valueOf(code);
		if (requestCode == RequestCode.UNKNOWN)
			throw new PacketException("Got error request code: "
					+ requestCode.name());
		short computedChecksum = CRC16.compute(code, dataLength, data);
		if (computedChecksum != checksum)
			throw new ChecksumMatchException();
	}

}
