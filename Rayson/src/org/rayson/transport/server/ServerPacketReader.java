package org.rayson.transport.server;

import java.nio.channels.SocketChannel;

import org.rayson.transport.common.CRC16;
import org.rayson.transport.common.ChecksumMatchException;
import org.rayson.transport.common.PacketException;
import org.rayson.transport.common.PacketReader;
import org.rayson.transport.common.RequestCode;

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
			throw new ChecksumMatchException(
					"Request packet checksum not match");
	}

}
