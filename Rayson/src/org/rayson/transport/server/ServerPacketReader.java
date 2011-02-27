package org.rayson.transport.server;

import java.nio.channels.SocketChannel;

import org.rayson.transport.common.CRC16;
import org.rayson.transport.common.ChecksumMatchException;
import org.rayson.transport.common.PacketException;
import org.rayson.transport.common.PacketReader;
import org.rayson.transport.common.RequestType;

class ServerPacketReader extends PacketReader {

	public ServerPacketReader(SocketChannel socketChannel) {
		super(socketChannel);
	}

	@Override
	protected void verifyPacket(byte type, short dataLength, short checksum,
			byte[] data) throws ChecksumMatchException, PacketException {
		RequestType requestType = RequestType.valueOf(type);
		if (requestType == RequestType.UNKNOWN)
			throw new PacketException("Got error request type: "
					+ requestType.name());
		short computedChecksum = CRC16.compute(type, dataLength, data);
		if (computedChecksum != checksum)
			throw new ChecksumMatchException(
					"Request packet checksum not match");
	}

}
