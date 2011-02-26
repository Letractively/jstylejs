package org.rayson.transport.client;

import java.nio.channels.SocketChannel;

import org.rayson.transport.common.CRC16;
import org.rayson.transport.common.ChecksumMatchException;
import org.rayson.transport.common.PacketException;
import org.rayson.transport.common.PacketReader;
import org.rayson.transport.common.ResponseCode;

final class ClientPacketReader extends PacketReader {

	public ClientPacketReader(SocketChannel socketChannel) {
		super(socketChannel);
	}

	@Override
	protected void verifyPacket(byte code, short dataLength, short checksum,
			byte[] data) throws ChecksumMatchException, PacketException {
		ResponseCode responseCode = ResponseCode.valueOf(code);
		if (responseCode != ResponseCode.OK)
			throw new PacketException("Got error response code: "
					+ responseCode.name());
		short computedChecksum = CRC16.compute(code, dataLength, data);
		if (computedChecksum != checksum)
			throw new ChecksumMatchException(
					"Response packet checksum not match");
	}
}
