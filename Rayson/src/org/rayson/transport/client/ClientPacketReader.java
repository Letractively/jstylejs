package org.rayson.transport.client;

import java.nio.channels.SocketChannel;

import org.rayson.transport.common.CRC16;
import org.rayson.transport.common.ChecksumMatchException;
import org.rayson.transport.common.PacketException;
import org.rayson.transport.common.PacketReader;
import org.rayson.transport.common.ResponseType;

final class ClientPacketReader extends PacketReader {

	public ClientPacketReader(SocketChannel socketChannel) {
		super(socketChannel);
	}

	@Override
	protected void verifyPacket(byte type, short dataLength, short checksum,
			byte[] data) throws ChecksumMatchException, PacketException {
		ResponseType responseType = ResponseType.valueOf(type);
		if (responseType != ResponseType.OK)
			throw new PacketException("Got error response type: "
					+ responseType.name());
		short computedChecksum = CRC16.compute(type, dataLength, data);
		if (computedChecksum != checksum)
			throw new ChecksumMatchException(
					"Response packet checksum not match");
	}
}
